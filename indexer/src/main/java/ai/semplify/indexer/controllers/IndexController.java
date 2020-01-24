package ai.semplify.indexer.controllers;

import ai.semplify.indexer.models.Doc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/index")
public class IndexController {

    @PostMapping(value = "files/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> indexFile(@RequestPart Doc doc, @RequestPart("file") FilePart filePart) {
        return Mono.empty();
    }
}
