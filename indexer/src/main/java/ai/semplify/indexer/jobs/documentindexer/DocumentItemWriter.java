package ai.semplify.indexer.jobs.documentindexer;

import ai.semplify.indexer.entities.postgresql.Document;
import ai.semplify.indexer.entities.postgresql.DocumentStatus;
import ai.semplify.indexer.entities.postgresql.DocumentType;
import ai.semplify.indexer.repositories.postgresql.DocumentJpaRepository;
import ai.semplify.indexer.services.IndexService;
import lombok.var;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class DocumentItemWriter implements ItemWriter<List<Document>> {

    private IndexService indexService;
    private DocumentJpaRepository documentJpaRepository;

    public DocumentItemWriter(IndexService indexService,
                              DocumentJpaRepository documentJpaRepository) {
        this.indexService = indexService;
        this.documentJpaRepository = documentJpaRepository;
    }

    @Override
    public void write(List<? extends List<Document>> list) {
        for (var documents : list) {
            for (var document : documents) {
                try {
                    if (document.getType().equalsIgnoreCase(DocumentType.FILE.getValue()))
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
            documentJpaRepository.saveAll(documents);
        }
    }
}
