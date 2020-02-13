package ai.semplify.indexer.services;

import ai.semplify.indexer.entities.postgresql.Document;
import ai.semplify.indexer.models.DocumentMetadata;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;

public interface IndexService {

    void submitFile(DocumentMetadata doc, MultipartFile filePart) throws IOException;

    void indexFile(Document document) throws TikaException, SAXException, IOException;


}
