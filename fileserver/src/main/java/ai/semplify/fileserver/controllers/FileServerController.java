package ai.semplify.fileserver.controllers;

import ai.semplify.fileserver.exceptions.FileNotFoundException;
import ai.semplify.commons.models.fileserver.File;
import ai.semplify.fileserver.services.FileService;
import lombok.var;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileServerController {

    private FileService fileService;

    public FileServerController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<File> upload(@RequestPart("file") MultipartFile filePart) throws IOException {
        return ResponseEntity.ok(fileService.store(filePart));
    }

    @GetMapping("/{fileId}/info")
    public ResponseEntity<File> info(@PathVariable("fileId") Long fileId) {
        return fileService.findInfoById(fileId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") Long fileId) {
        try {
            var file = fileService.findById(fileId)
                    .orElseThrow(ai.semplify.fileserver.exceptions.FileNotFoundException::new);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(new ByteArrayResource(file.getContent()));

        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/anon/{fileId}")
    public ResponseEntity<Resource> anonDownload(@PathVariable("fileId") Long fileId) {
        return download(fileId);
    }

}
