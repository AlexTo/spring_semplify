package ai.semplify.indexer.controllers;

import ai.semplify.indexer.models.SuggestionRequest;
import ai.semplify.indexer.services.SuggestionService;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/suggestion")
public class SuggestionController {

    private SuggestionService suggestionService;

    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @PostMapping("")
    public ResponseEntity<SearchResponse> suggest(SuggestionRequest request) {
        return ResponseEntity.ok(suggestionService.suggestSubject(request));
    }


}
