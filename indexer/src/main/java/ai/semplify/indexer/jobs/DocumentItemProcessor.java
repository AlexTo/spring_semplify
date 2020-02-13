package ai.semplify.indexer.jobs;

import ai.semplify.indexer.entities.postgresql.Document;
import ai.semplify.indexer.entities.postgresql.DocumentStatus;
import ai.semplify.indexer.repositories.postgresql.DocumentRepository;
import lombok.var;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class DocumentItemProcessor implements ItemProcessor<List<Document>, List<Document>> {

    private DocumentRepository documentRepository;

    public DocumentItemProcessor(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public List<Document> process(List<Document> documents) throws Exception {

        for (var document : documents) {
            document.setError(null);
            document.setStatus(DocumentStatus.PROCESSING.getValue());
        }
        return documentRepository.saveAll(documents);
    }
}
