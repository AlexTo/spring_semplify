package ai.semplify.indexer.jobs.subjectindexer;

import ai.semplify.indexer.entities.elasticsearch.Subject;
import lombok.var;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import java.util.ArrayList;
import java.util.List;

public class SubjectItemReader implements ItemReader<List<Subject>> {

    private Repository sparqlEndpoint;
    private Long page = 0L;
    private Long size = 1000L;
    private Logger logger = LoggerFactory.getLogger(SubjectItemWriter.class);

    public SubjectItemReader(Repository sparqlEndpoint) {
        this.sparqlEndpoint = sparqlEndpoint;
    }

    @Override
    public List<Subject> read() {
        var query = "SELECT DISTINCT ?s WHERE { ?s ?p ?o } " +
                "LIMIT " + size +
                " OFFSET " + page * size;
        try (RepositoryConnection con = sparqlEndpoint.getConnection()) {
            var tupleQuery = con.prepareTupleQuery(query);
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                if (!result.hasNext())
                    return null;
                var subjects = new ArrayList<Subject>();
                while (result.hasNext()) {
                    var row = result.next();
                    var s = row.getBinding("s");
                    var subject = new Subject();
                    subject.setUri(s.getValue().stringValue());
                    subjects.add(subject);
                }
                page++;
                logger.info("Indexed " + page * size + " triple subjects.");
                return subjects;
            }
        }
    }
}
