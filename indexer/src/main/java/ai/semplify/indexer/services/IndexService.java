package ai.semplify.indexer.services;

import ai.semplify.indexer.entities.postgresql.Document;
import ai.semplify.indexer.models.DocumentMetadata;
import org.apache.tika.exception.TikaException;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;

public interface IndexService {

    void submitFile(DocumentMetadata doc, MultipartFile filePart) throws IOException;

    void indexFile(Document document) throws TikaException, SAXException, IOException;

    void startFullSubjectIndex() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException;

}
