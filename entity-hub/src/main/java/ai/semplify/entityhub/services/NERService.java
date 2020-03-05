package ai.semplify.entityhub.services;

import ai.semplify.entityhub.models.Annotation;
import ai.semplify.entityhub.models.TextAnnotationRequest;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;

public interface NERService {
    Annotation annotateText(TextAnnotationRequest textAnnotationRequest) throws IOException;

    Annotation annotateFile(MultipartFile filePart) throws IOException, TikaException, SAXException;

    Annotation annotateServerFile(Long fileId) throws IOException, TikaException, SAXException;
}
