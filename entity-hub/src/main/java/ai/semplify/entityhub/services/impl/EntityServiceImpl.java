package ai.semplify.entityhub.services.impl;

import ai.semplify.entityhub.mappers.DepictionMapper;
import ai.semplify.entityhub.mappers.PrefLabelMapper;
import ai.semplify.entityhub.models.*;
import ai.semplify.entityhub.repositories.redis.DepictionCache;
import ai.semplify.entityhub.repositories.redis.PrefLabelCache;
import ai.semplify.entityhub.services.EntityService;
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

    private PrefLabelMapper prefLabelMapper;
    private DepictionMapper depictionMapper;

    private Logger logger = LoggerFactory.getLogger(EntityServiceImpl.class);

    public EntityServiceImpl(Repository sparqlEndpoint,
                             PrefLabelCache prefLabelCache,
                             DepictionCache depictionCache,
                             PrefLabelMapper prefLabelMapper,
                             DepictionMapper depictionMapper) {
        this.sparqlEndpoint = sparqlEndpoint;
        valueFactory = sparqlEndpoint.getValueFactory();
        this.prefLabelCache = prefLabelCache;
        this.depictionCache = depictionCache;
        this.prefLabelMapper = prefLabelMapper;
        this.depictionMapper = depictionMapper;
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
}
