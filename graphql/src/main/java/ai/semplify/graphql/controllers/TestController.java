package ai.semplify.graphql.controllers;

import ai.semplify.graphql.models.Doc;
import ai.semplify.graphql.services.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
public class TestController {

    private IndexService indexService;

    public TestController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping("")
    public Flux<Doc> test() {
        return indexService.findAll();
    }
}
