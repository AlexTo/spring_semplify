package ai.semplify.entityhub.services.impl;

import ai.semplify.entityhub.models.TypeCheckRequest;
import ai.semplify.entityhub.models.TypeCheckResponse;
import ai.semplify.entityhub.services.EntityService;
import lombok.var;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.stereotype.Service;

@Service
public class EntityServiceImpl implements EntityService {

    private Repository repo;
    private ValueFactory valueFactory;

    public EntityServiceImpl(Repository repo) {
        this.repo = repo;
        valueFactory = repo.getValueFactory();
    }

    @Override
    public TypeCheckResponse isSubClassOf(TypeCheckRequest request) {
        var query = "SELECT * WHERE { " +
                "  ?entity a ?someType . " +
                "  ?someType rdfs:subClassOf* ?superClass " +
                "  FILTER (?superClass = ?type) " +
                "}";
        try (RepositoryConnection con = repo.getConnection()) {
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
        try (RepositoryConnection con = repo.getConnection()) {
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
}
