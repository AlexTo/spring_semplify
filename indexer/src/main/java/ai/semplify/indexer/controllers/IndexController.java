package ai.semplify.indexer.controllers;

import ai.semplify.commons.models.indexer.DocumentMetadata;
import ai.semplify.indexer.services.IndexService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/index")
public class IndexController {

    private ObjectMapper objectMapper;
    private IndexService indexService;

    public IndexController(ObjectMapper objectMapper, IndexService indexService) {
        this.objectMapper = objectMapper;
        this.indexService = indexService;
    }

    @PostMapping(value = "files/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> submitFile(@RequestPart String documentMetadata,
                                           @RequestParam("file") MultipartFile filePart)
            throws IOException {
        //TODO: Configure Message Converters to automatically convert string to json
        var docObj = objectMapper.readValue(documentMetadata, DocumentMetadata.class);
        indexService.submitFile(docObj, filePart);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/fullSubjectIndex")
    public ResponseEntity<Void> fullIndex()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        indexService.startFullSubjectIndex();
        return ResponseEntity.ok().build();
    }

}
