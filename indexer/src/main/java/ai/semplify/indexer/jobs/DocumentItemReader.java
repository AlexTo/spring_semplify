package ai.semplify.indexer.jobs;

import ai.semplify.indexer.entities.postgresql.Document;
import ai.semplify.indexer.repositories.postgresql.DocumentRepository;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class DocumentItemReader implements ItemReader<List<Document>> {

    private DocumentRepository documentRepository;
    private Logger logger = LoggerFactory.getLogger(DocumentItemReader.class);

    public DocumentItemReader(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }


    @Override
    public List<Document> read() {
        var documents = documentRepository
                .findAllByStatusIsNullOrderByLastModifiedDateAsc(PageRequest.of(0, 10));

        if (documents.isEmpty()) {
            logger.info("Found no new documents");
            return null;
        } else {
            logger.info("Indexing " + documents.size() + " documents.");
            return documents;
        }
    }
}
