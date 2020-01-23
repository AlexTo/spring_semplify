package ai.semplify.entityhub.services;

import ai.semplify.entityhub.models.Annotation;
import ai.semplify.entityhub.models.TextAnnotationRequest;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface NERService {
    Mono<Annotation> annotateText(TextAnnotationRequest textAnnotationRequest);

    Mono<Annotation> annotateFile(FilePart filePart);
}
