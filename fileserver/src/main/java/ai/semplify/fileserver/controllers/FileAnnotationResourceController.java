package ai.semplify.fileserver.controllers;

import ai.semplify.commons.models.entityhub.AnnotationResource;
import ai.semplify.fileserver.exceptions.FileNotFoundException;
import ai.semplify.fileserver.services.FileAnnotationService;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/file-annotation-resources")
public class FileAnnotationResourceController {
    private FileAnnotationService fileAnnotationService;

    public FileAnnotationResourceController(FileAnnotationService fileAnnotationService) {
        this.fileAnnotationService = fileAnnotationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<AnnotationResource>> findAll(@RequestParam Long fileAnnotationId) throws FileNotFoundException {
        var result = fileAnnotationService.findAllAnnotationResources(fileAnnotationId);
        return ResponseEntity.ok(result);
    }
}
