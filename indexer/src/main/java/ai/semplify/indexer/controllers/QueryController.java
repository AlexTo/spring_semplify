package ai.semplify.indexer.controllers;

import ai.semplify.indexer.models.Doc;
import ai.semplify.indexer.services.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/search")
public class QueryController {

    private IndexService indexService;

    public QueryController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping("")
    public Flux<Doc> findAll() {
        return indexService.findAll();
    }
}
