package ai.semplify.indexer.controllers;

import ai.semplify.indexer.models.Doc;
import ai.semplify.indexer.services.IndexService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> indexFile(@RequestPart String doc, @RequestPart("file") FilePart filePart) throws JsonProcessingException {
        //TODO: Configure Message Converters to automatically convert string to json
        var docObj = objectMapper.readValue(doc, Doc.class);
        return indexService.indexFile(docObj, filePart);
    }


}
