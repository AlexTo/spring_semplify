package ai.semplify.indexer.jobs;

import ai.semplify.indexer.entities.postgresql.Document;
import ai.semplify.indexer.entities.postgresql.DocumentStatus;
import ai.semplify.indexer.repositories.postgresql.DocumentRepository;
import ai.semplify.indexer.services.IndexService;
import lombok.var;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class DocumentItemWriter implements ItemWriter<List<Document>> {

    private IndexService indexService;
    private DocumentRepository documentRepository;

    public DocumentItemWriter(IndexService indexService,
                              DocumentRepository documentRepository) {
        this.indexService = indexService;
        this.documentRepository = documentRepository;
    }

    @Override
    public void write(List<? extends List<Document>> list) {
        for (var documents : list) {
            for (var document : documents) {
                try {
                    indexService.indexFile(document);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e.getMessage() != null) {
                        document.setError(e.getMessage());
                    } else {
                        document.setError("UNKNOWN ERROR");
                    }
                }
                document.setStatus(DocumentStatus.DONE.getValue());
            }
            documentRepository.saveAll(documents);
        }
    }
}
