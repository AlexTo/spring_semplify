package ai.semplify.indexer.controllers;

import ai.semplify.commons.models.indexer.Query;
import ai.semplify.commons.models.indexer.SearchHits;
import ai.semplify.indexer.services.SearchService;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/search")
public class SearchController {

    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("")
    public ResponseEntity<SearchHits> search(@Valid @RequestBody Query query) {
        var results = searchService.search(query);
        return ResponseEntity.ok(results);
    }
}
