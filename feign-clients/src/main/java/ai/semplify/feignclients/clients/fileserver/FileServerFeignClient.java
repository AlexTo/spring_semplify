package ai.semplify.feignclients.clients.fileserver;

import ai.semplify.feignclients.clients.fileserver.models.File;
import ai.semplify.feignclients.config.DefaultFeignClientConfiguration;
import ai.semplify.feignclients.config.MultipartSupportConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "file-server", configuration = {
        MultipartSupportConfiguration.class,
        DefaultFeignClientConfiguration.class})
public interface FileServerFeignClient {
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    File upload(@RequestPart("file") MultipartFile filePart);

    @GetMapping("/{fileId}/info")
    File info(@PathVariable("fileId") Long fileId);

    @GetMapping("/{fileId}")
    MultipartFile download(@PathVariable("fileId") Long fileId);
}
