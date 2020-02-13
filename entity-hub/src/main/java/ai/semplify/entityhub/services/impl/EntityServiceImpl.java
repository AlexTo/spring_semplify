package ai.semplify.entityhub.services.impl;

import ai.semplify.entityhub.models.TypeCheckRequest;
import ai.semplify.entityhub.models.TypeCheckResponse;
import ai.semplify.entityhub.services.EntityService;
import lombok.var;
import org.eclipse.rdf4j.model.ValueFactory;
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
    public TypeCheckResponse isOfType(TypeCheckRequest request) {
        return null;
    }

    @Override
    public TypeCheckResponse isSubClassOfTransitive(TypeCheckRequest request) {
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
}
