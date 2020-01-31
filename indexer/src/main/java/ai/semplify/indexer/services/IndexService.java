package ai.semplify.indexer.services;

import ai.semplify.indexer.models.Doc;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface IndexService {

    Mono<String> indexFile(Doc doc, FilePart filePart);

}
