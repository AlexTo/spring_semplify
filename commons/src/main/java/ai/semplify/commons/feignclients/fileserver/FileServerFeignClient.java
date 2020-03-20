package ai.semplify.commons.feignclients.fileserver;

import ai.semplify.commons.feignclients.DefaultFeignClientConfiguration;
import ai.semplify.commons.feignclients.MultipartSupportConfiguration;
import ai.semplify.commons.models.entityhub.AnnotationResource;
import ai.semplify.commons.models.fileserver.File;
import ai.semplify.commons.models.fileserver.FileAnnotation;
import ai.semplify.commons.models.fileserver.FileAnnotationPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "file-server", configuration = {
        MultipartSupportConfiguration.class,
        DefaultFeignClientConfiguration.class})
public interface FileServerFeignClient {
    @PostMapping("/files/")
    @ResponseStatus(HttpStatus.CREATED)
    File upload(@RequestPart("file") MultipartFile filePart);

    @GetMapping("/files/{fileId}/info")
    File info(@PathVariable("fileId") Long fileId);

    @GetMapping("/files/{fileId}")
    MultipartFile download(@PathVariable("fileId") Long fileId);

    @PostMapping("/file-annotations/")
    FileAnnotation createAnnotations(@Valid @RequestBody FileAnnotation fileAnnotation);

    @GetMapping("/file-annotations/")
    FileAnnotationPage findAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                               @RequestParam(required = false, defaultValue = "20") Integer size);

    @GetMapping("/file-annotation-resources/")
    List<AnnotationResource> findAll(@RequestParam Long fileAnnotationId);

    @PutMapping("/{fileAnnotationId}")
    FileAnnotation update(@PathVariable("fileAnnotationId") Long fileAnnotationId,
                          @Valid @RequestBody FileAnnotation fileAnnotation);
}
