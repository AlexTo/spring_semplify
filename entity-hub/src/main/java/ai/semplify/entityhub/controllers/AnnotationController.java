package ai.semplify.entityhub.controllers;

import ai.semplify.entityhub.models.Annotation;
import ai.semplify.entityhub.models.TextAnnotationRequest;
import ai.semplify.entityhub.services.NERService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/ner")
public class AnnotationController {

    private NERService nerService;
    private Logger logger = LoggerFactory.getLogger(AnnotationController.class);

    public AnnotationController(NERService nerService) {
        this.nerService = nerService;
    }

    @GetMapping("/test")
    public Mono<String> test(Principal principal) {
        return Mono.just("hello " + principal.getName());
    }

    @PostMapping("text")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Annotation> annotateText(@Valid @RequestBody TextAnnotationRequest textAnnotationRequest) {
        return nerService.annotateText(textAnnotationRequest);
    }

    @PostMapping("file")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Annotation> annotateFile(@RequestPart("file") FilePart filePart) {
        return nerService.annotateFile(filePart);
    }
}
