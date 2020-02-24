package ai.semplify.fileserver.controllers;

import ai.semplify.fileserver.models.File;
import ai.semplify.fileserver.services.FileService;
import org.springframework.http.HttpStatus;
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

}
