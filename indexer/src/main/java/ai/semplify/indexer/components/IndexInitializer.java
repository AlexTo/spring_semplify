package ai.semplify.indexer.components;

import ai.semplify.indexer.entities.elasticsearch.Document;
import ai.semplify.indexer.entities.elasticsearch.Subject;
import lombok.var;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

@Component
public class IndexInitializer {

    public IndexInitializer(ElasticsearchOperations elasticsearchOperations,
                            @Qualifier("documents_index") IndexCoordinates documentsIndex,
                            @Qualifier("subjects_index") IndexCoordinates subjectsIndex) {

        var documentsIndexOps = elasticsearchOperations.indexOps(documentsIndex);
        if (!documentsIndexOps.exists()) {
            documentsIndexOps.create();
        }
        documentsIndexOps.putMapping(documentsIndexOps.createMapping(Document.class));
        documentsIndexOps.refresh();

        var subjectsIndexOps = elasticsearchOperations.indexOps(subjectsIndex);
        if (!subjectsIndexOps.exists()) {
            subjectsIndexOps.create();
        }
        subjectsIndexOps.putMapping(subjectsIndexOps.createMapping(Subject.class));
        subjectsIndexOps.refresh();
    }
}
