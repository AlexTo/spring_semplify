package ai.semplify.entityhub.services.impl;

import ai.semplify.entityhub.mappers.AbstractMapper;
import ai.semplify.entityhub.mappers.DepictionMapper;
import ai.semplify.entityhub.mappers.PrefLabelMapper;
import ai.semplify.entityhub.mappers.ThumbnailMapper;
import ai.semplify.entityhub.models.*;
import ai.semplify.entityhub.repositories.redis.AbstractCache;
import ai.semplify.entityhub.repositories.redis.DepictionCache;
import ai.semplify.entityhub.repositories.redis.PrefLabelCache;
import ai.semplify.entityhub.repositories.redis.ThumbnailCache;
import ai.semplify.entityhub.services.EntityService;
import ai.semplify.entityhub.vocab.DBO;
import lombok.var;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EntityServiceImpl implements EntityService {

    private Repository sparqlEndpoint;
    private ValueFactory valueFactory;
    private PrefLabelCache prefLabelCache;
    private DepictionCache depictionCache;
    private ThumbnailCache thumbnailCache;
    private AbstractCache abstractCache;

    private PrefLabelMapper prefLabelMapper;
    private DepictionMapper depictionMapper;
    private ThumbnailMapper thumbnailMapper;
    private AbstractMapper abstractMapper;

    private Logger logger = LoggerFactory.getLogger(EntityServiceImpl.class);

    public EntityServiceImpl(Repository sparqlEndpoint,
                             PrefLabelCache prefLabelCache,
                             DepictionCache depictionCache,
                             ThumbnailCache thumbnailCache,
                             AbstractCache abstractCache,
                             PrefLabelMapper prefLabelMapper,
                             DepictionMapper depictionMapper,
                             ThumbnailMapper thumbnailMapper,
                             AbstractMapper abstractMapper) {
        this.sparqlEndpoint = sparqlEndpoint;
        valueFactory = sparqlEndpoint.getValueFactory();

        this.prefLabelCache = prefLabelCache;
        this.depictionCache = depictionCache;
        this.thumbnailCache = thumbnailCache;
        this.abstractCache = abstractCache;
        this.prefLabelMapper = prefLabelMapper;
        this.depictionMapper = depictionMapper;
        this.thumbnailMapper = thumbnailMapper;
        this.abstractMapper = abstractMapper;
    }

    @Override
    public TypeCheckResponse isSubClassOf(TypeCheckRequest request) {
        var query = "SELECT * WHERE { " +
                "  ?entity a ?someType . " +
                "  ?someType rdfs:subClassOf* ?superClass " +
                "  FILTER (?superClass = ?type) " +
                "}";
        try (RepositoryConnection con = sparqlEndpoint.getConnection()) {
            var tupleQuery = con.prepareTupleQuery(query);
            tupleQuery.setBinding("entity", valueFactory.createIRI(request.getEntity()));
            tupleQuery.setBinding("type", valueFactory.createIRI(request.getType()));
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                var response = new TypeCheckResponse();
                response.setAns(result.hasNext());
                return response;
            }
        }
    }

    @Override
    public TypeCheckResponse isNarrowerConceptOf(TypeCheckRequest request) {

        var query = "PREFIX skos: <" + SKOS.NAMESPACE + "> \n" +
                "SELECT * WHERE { " +
                "  ?entity a  skos:Concept . " +
                "  ?entity skos:broader* ?someConcept " +
                "  FILTER (?someConcept = ?type) " +
                "}";
        try (RepositoryConnection con = sparqlEndpoint.getConnection()) {
            var tupleQuery = con.prepareTupleQuery(query);
            tupleQuery.setBinding("entity", valueFactory.createIRI(request.getEntity()));
            tupleQuery.setBinding("type", valueFactory.createIRI(request.getType()));
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                var response = new TypeCheckResponse();
                response.setAns(result.hasNext());
                return response;
            }
        }
    }

    @Override
    public PrefLabelResponse getPrefLabel(PrefLabelRequest request) {

        var uri = request.getUri();
        if (uri == null) {
            return null;
        }

        var fromCache = prefLabelCache.findById(uri);
        if (fromCache.isPresent()) {
            var cachedPrefLabel = fromCache.get();
            logger.info("PrefLabel cache hit: " + uri + " => " + cachedPrefLabel.getPrefLabel());
            return prefLabelMapper.toModel(cachedPrefLabel);
        }

        logger.info("PrefLabel cache missed: " + uri);

        var predicates = new ArrayList<String>();
        predicates.add(RDFS.LABEL.stringValue());
        predicates.add(SKOS.PREF_LABEL.stringValue());
        predicates.add(FOAF.NAME.stringValue());

        StringBuilder query = new StringBuilder("SELECT * WHERE { ");

        var i = 0;
        for (var predicate : predicates) {
            query.append("OPTIONAL { ?entity <").append(predicate).append("> ?label").append(++i).append(" } . \n");
        }
        query.append(" }");

        var prefLabelResponse = new PrefLabelResponse();

        try (RepositoryConnection con = sparqlEndpoint.getConnection()) {
            var tupleQuery = con.prepareTupleQuery(query.toString());
            tupleQuery.setBinding("entity", valueFactory.createIRI(uri));
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                while (result.hasNext()) {
                    var bindings = result.next();
                    for (var binding : bindings) {
                        var label = binding.getValue().stringValue();
                        var lang = ((Literal) binding.getValue()).getLanguage().orElse(null);
                        if (lang == null) {
                            prefLabelResponse.setPrefLabel(label);
                            prefLabelResponse.setLang(null);
                            return prefLabelResponse;
                        } else if (lang.equalsIgnoreCase("en")) {
                            if (!"en".equalsIgnoreCase(prefLabelResponse.getLang())) {
                                prefLabelResponse.setPrefLabel(label);
                                prefLabelResponse.setLang(lang);
                            }
                        } else if (lang.toLowerCase().startsWith("en")) {
                            if (!"en".equalsIgnoreCase(prefLabelResponse.getLang())) {
                                prefLabelResponse.setPrefLabel(label);
                                prefLabelResponse.setLang(lang);
                            }
                        } else {
                            if (prefLabelResponse.getLang() == null ||
                                    (prefLabelResponse.getLang() != null
                                            && !prefLabelResponse.getLang().toLowerCase().startsWith("en"))) {
                                prefLabelResponse.setPrefLabel(label);
                                prefLabelResponse.setLang(lang);
                            }
                        }
                    }
                }
                if (prefLabelResponse.getPrefLabel() == null) {
                    var separator = uri.lastIndexOf("#");
                    if (uri.lastIndexOf("/") > separator)
                        separator = uri.lastIndexOf("/") + 1;
                    if (separator < 0)
                        separator = 0;
                    if (separator < uri.length())
                        prefLabelResponse.setPrefLabel(uri.substring(separator));
                    else
                        prefLabelResponse.setPrefLabel(uri);
                }

                var toCache = prefLabelMapper.toEntity(prefLabelResponse);
                toCache.setUri(uri);
                prefLabelCache.save(toCache);
                return prefLabelResponse;
            }
        }
    }

    @Override
    public PrefLabelResponse getPrefLabel(String uri) {
        var prefLabelRequest = new PrefLabelRequest();
        prefLabelRequest.setUri(uri);
        return getPrefLabel(prefLabelRequest);
    }

    @Override
    public DepictionResponse getDepiction(DepictionRequest request) {
        var uri = request.getUri();
        if (uri == null) {
            return null;
        }

        var fromCache = depictionCache.findById(uri);
        if (fromCache.isPresent()) {
            var cachedDepiction = fromCache.get();
            logger.info("Depiction cache hit: " + uri + " => " + cachedDepiction.getDepictionUri());
            return depictionMapper.toModel(cachedDepiction);
        }

        logger.info("Depiction cache missed: " + uri);

        var predicates = new ArrayList<String>();
        predicates.add(FOAF.DEPICTION.stringValue());

        StringBuilder query = new StringBuilder("SELECT * WHERE { ");

        var i = 0;
        for (var predicate : predicates) {
            query.append("OPTIONAL { ?entity <").append(predicate).append("> ?depiction").append(++i).append(" } . \n");
        }
        query.append(" }");

        var depictionResponse = new DepictionResponse();

        try (RepositoryConnection con = sparqlEndpoint.getConnection()) {
            var tupleQuery = con.prepareTupleQuery(query.toString());
            tupleQuery.setBinding("entity", valueFactory.createIRI(uri));
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                while (result.hasNext()) {
                    var bindings = result.next();
                    for (var binding : bindings) {
                        depictionResponse.setDepictionUri(binding.getValue().stringValue());

                        var toCache = depictionMapper.toEntity(depictionResponse);
                        toCache.setUri(uri);
                        depictionCache.save(toCache);
                        return depictionResponse;
                    }
                }
            }
        }
        return depictionResponse;
    }

    @Override
    public DepictionResponse getDepiction(String uri) {
        var request = new DepictionRequest();
        request.setUri(uri);
        return getDepiction(request);
    }

    @Override
    public ThumbnailResponse getThumbnail(ThumbnailRequest request) {
        var uri = request.getUri();
        if (uri == null) {
            return null;
        }

        var fromCache = thumbnailCache.findById(uri);
        if (fromCache.isPresent()) {
            var cachedThumbnail = fromCache.get();
            logger.info("Thumbnail cache hit: " + uri + " => " + cachedThumbnail.getThumbnailUri());
            return thumbnailMapper.toModel(cachedThumbnail);
        }

        logger.info("Thumbnail cache missed: " + uri);

        var predicates = new ArrayList<String>();
        predicates.add(DBO.THUMBNAIL.stringValue());

        StringBuilder query = new StringBuilder("SELECT * WHERE { ");

        var i = 0;
        for (var predicate : predicates) {
            query.append("OPTIONAL { ?entity <").append(predicate).append("> ?thumbnail").append(++i).append(" } . \n");
        }
        query.append(" }");

        var thumbnailResponse = new ThumbnailResponse();

        try (RepositoryConnection con = sparqlEndpoint.getConnection()) {
            var tupleQuery = con.prepareTupleQuery(query.toString());
            tupleQuery.setBinding("entity", valueFactory.createIRI(uri));
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                while (result.hasNext()) {
                    var bindings = result.next();
                    for (var binding : bindings) {
                        thumbnailResponse.setThumbnailUri(binding.getValue().stringValue());

                        var toCache = thumbnailMapper.toEntity(thumbnailResponse);
                        toCache.setUri(uri);
                        thumbnailCache.save(toCache);
                        return thumbnailResponse;
                    }
                }
            }
        }
        return thumbnailResponse;
    }

    @Override
    public ThumbnailResponse getThumbnail(String uri) {
        var request = new ThumbnailRequest();
        request.setUri(uri);
        return getThumbnail(request);
    }

    @Override
    public AbstractResponse getAbstract(AbstractRequest request) {
        var uri = request.getUri();
        if (uri == null) {
            return null;
        }

        var fromCache = abstractCache.findById(uri);
        if (fromCache.isPresent()) {
            var cachedAbstract = fromCache.get();
            logger.info("Abstract cache hit: " + uri);
            return abstractMapper.toModel(cachedAbstract);
        }

        logger.info("Abstract cache missed: " + uri);

        var predicates = new ArrayList<String>();
        predicates.add(RDFS.COMMENT.stringValue());
        predicates.add(DBO.ABSTRACT.stringValue());

        StringBuilder query = new StringBuilder("SELECT * WHERE { ");

        var i = 0;
        for (var predicate : predicates) {
            query.append("OPTIONAL { ?entity <").append(predicate).append("> ?abstract").append(++i).append(" } . \n");
        }
        query.append(" }");

        var abstractResponse = new AbstractResponse();

        try (RepositoryConnection con = sparqlEndpoint.getConnection()) {
            var tupleQuery = con.prepareTupleQuery(query.toString());
            tupleQuery.setBinding("entity", valueFactory.createIRI(uri));
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                while (result.hasNext()) {
                    var bindings = result.next();
                    for (var binding : bindings) {
                        abstractResponse.setText(binding.getValue().stringValue());
                        var toCache = abstractMapper.toEntity(abstractResponse);
                        toCache.setUri(uri);
                        abstractCache.save(toCache);
                        return abstractResponse;
                    }
                }
            }
        }
        return abstractResponse;
    }

    @Override
    public AbstractResponse getAbstract(String uri) {
        var request = new AbstractRequest();
        request.setUri(uri);
        return getAbstract(request);
    }

    @Override
    public EntitySummaryResponse getSummary(String uri) {
        var request = new EntitySummaryRequest();
        request.setUri(uri);
        return getSummary(request);
    }

    @Override
    public EntitySummaryResponse getSummary(EntitySummaryRequest request) {
        var uri = request.getUri();
        var response = new EntitySummaryResponse();
        var thumbnail = getThumbnail(uri);
        var prefLabel = getPrefLabel(uri);
        var abstract_ = getAbstract(uri);
        response.setThumbnail(thumbnail);
        response.setPrefLabel(prefLabel);
        response.setAbstract_(abstract_);
        return response;
    }

    @Override
    public void invalidateCache(String uri) {
        thumbnailCache.deleteById(uri);
        prefLabelCache.deleteById(uri);
        abstractCache.deleteById(uri);
    }
}
