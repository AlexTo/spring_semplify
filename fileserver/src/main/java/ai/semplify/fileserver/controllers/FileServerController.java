package ai.semplify.fileserver.controllers;

import ai.semplify.fileserver.models.File;
import ai.semplify.fileserver.services.FileService;
import lombok.var;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/files")
public class FileServerController {

    private FileService fileService;

    public FileServerController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("test")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> test() {
        return Mono.just("hello");
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<File> upload(@RequestPart("file") FilePart filePart) {
        return fileService.store(filePart);
    }

    @GetMapping("/{fileId}/info")
    public ResponseEntity<Mono<File>> info(@PathVariable("fileId") Long fileId) {
        return fileService.findInfoById(fileId)
                .map(fileInfo -> ResponseEntity.ok(Mono.just(fileInfo)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
