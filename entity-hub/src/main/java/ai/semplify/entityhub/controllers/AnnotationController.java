package ai.semplify.entityhub.controllers;

import ai.semplify.entityhub.models.Annotation;
import ai.semplify.entityhub.models.TextAnnotationRequest;
import ai.semplify.entityhub.services.NERService;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/ner")
public class AnnotationController {

    private NERService nerService;
    private Logger logger = LoggerFactory.getLogger(AnnotationController.class);

    public AnnotationController(NERService nerService) {
        this.nerService = nerService;
    }

    @PostMapping("text")
    public ResponseEntity<Annotation> annotateText(@Valid @RequestBody TextAnnotationRequest textAnnotationRequest)
            throws IOException {
        return ResponseEntity.ok(nerService.annotateText(textAnnotationRequest));
    }

    @PostMapping("file")
    public ResponseEntity<Annotation> annotateFile(@RequestParam("file") MultipartFile filePart)
            throws TikaException, IOException, SAXException {
        return ResponseEntity.ok(nerService.annotateFile(filePart));
    }

    @PostMapping("serverFile")
    public ResponseEntity<Annotation> annotateServerFile(@RequestBody Long fileId)
            throws TikaException, IOException, SAXException {
        return ResponseEntity.ok(nerService.annotateServerFile(fileId));
    }


}
