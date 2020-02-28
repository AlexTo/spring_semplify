package ai.semplify.indexer.jobs.subjectindexer;

import ai.semplify.indexer.entities.elasticsearch.Subject;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class SubjectItemWriter implements ItemWriter<List<Subject>> {

    private IndexCoordinates subjectsIndex;
    private ElasticsearchOperations elasticsearchOperations;

    public SubjectItemWriter(IndexCoordinates subjectsIndex,
                             ElasticsearchOperations elasticsearchOperations) {
        this.subjectsIndex = subjectsIndex;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public void write(List<? extends List<Subject>> list) throws Exception {
        var indexQueries = new ArrayList<IndexQuery>();
        for (var subjects : list) {
            for (var subject : subjects) {
                var indexQuery = new IndexQueryBuilder()
                        .withId(subject.getUri())
                        .withObject(subject)
                        .build();
                indexQueries.add(indexQuery);

            }
        }
        elasticsearchOperations.bulkIndex(indexQueries, subjectsIndex);
    }
}
