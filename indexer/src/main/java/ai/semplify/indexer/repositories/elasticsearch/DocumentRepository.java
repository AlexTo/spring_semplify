package ai.semplify.indexer.repositories.elasticsearch;

import ai.semplify.indexer.entities.elasticsearch.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DocumentRepository extends ElasticsearchRepository<Document, String> {
}
