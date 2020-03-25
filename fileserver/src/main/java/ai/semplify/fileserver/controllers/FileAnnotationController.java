package ai.semplify.fileserver.controllers;

import ai.semplify.commons.models.fileserver.FileAnnotation;
import ai.semplify.commons.models.fileserver.FileAnnotationUpdate;
import ai.semplify.commons.models.fileserver.FileAnnotationPage;
import ai.semplify.fileserver.exceptions.FileAnnotationNotFoundException;
import ai.semplify.fileserver.exceptions.FileNotFoundException;
import ai.semplify.fileserver.services.FileAnnotationService;
import lombok.var;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/file-annotations")
public class FileAnnotationController {

    private FileAnnotationService fileAnnotationService;

    public FileAnnotationController(FileAnnotationService fileAnnotationService) {
        this.fileAnnotationService = fileAnnotationService;
    }

    @PostMapping("/")
    public ResponseEntity<FileAnnotation> create(@Valid @RequestBody FileAnnotation fileAnnotation)
            throws FileNotFoundException {
        return ResponseEntity.ok(fileAnnotationService.create(fileAnnotation));
    }

    @GetMapping("/{fileAnnotationId}")
    public ResponseEntity<FileAnnotation> findOne(@PathVariable("fileAnnotationId") Long fileAnnotationId) throws FileNotFoundException {
        return ResponseEntity.ok(fileAnnotationService.findById(fileAnnotationId));
    }

    @GetMapping("/")
    public ResponseEntity<FileAnnotationPage> findAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "20") Integer size) {
        var pageable = PageRequest.of(page, size);
        var result = fileAnnotationService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{fileAnnotationId}")
    public ResponseEntity<FileAnnotation> update(@PathVariable("fileAnnotationId") Long fileAnnotationId,
                                                 @Valid @RequestBody FileAnnotationUpdate fileAnnotation) throws FileAnnotationNotFoundException {
        return ResponseEntity.ok(fileAnnotationService.update(fileAnnotationId, fileAnnotation));
    }
}
