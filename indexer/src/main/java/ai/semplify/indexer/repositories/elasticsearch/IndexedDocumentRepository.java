package ai.semplify.indexer.repositories.elasticsearch;

import ai.semplify.indexer.entities.elasticsearch.IndexedDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IndexedDocumentRepository extends ElasticsearchRepository<IndexedDocument, String> {
}
