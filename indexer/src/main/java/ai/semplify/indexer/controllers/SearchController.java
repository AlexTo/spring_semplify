package ai.semplify.indexer.controllers;

import ai.semplify.indexer.models.Query;
import ai.semplify.indexer.models.SearchHits;
import ai.semplify.indexer.services.SearchService;
import lombok.var;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/search")
public class SearchController {

    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("")
    public Mono<SearchHits> search(@Valid @RequestBody Query query) {

        var page = query.getPage() == null ? 0 : query.getPage();
        var size = query.getSize() == null ? 100 : query.getSize();
        var pageRequest = PageRequest.of(page, size);
        var results = searchService.search(query.getQ(), pageRequest);
        return Mono.just(results);
    }
}
