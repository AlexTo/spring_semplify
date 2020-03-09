package ai.semplify.commons.feignclients.fileserver;

import ai.semplify.commons.feignclients.DefaultFeignClientConfiguration;
import ai.semplify.commons.feignclients.MultipartSupportConfiguration;
import ai.semplify.commons.models.fileserver.File;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
