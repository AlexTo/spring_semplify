package ai.semplify.indexer.controllers;

import ai.semplify.commons.models.indexer.SuggestionRequest;
import ai.semplify.commons.models.indexer.Suggestions;
import ai.semplify.indexer.services.SuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/suggest")
public class SuggestionController {

    private SuggestionService suggestionService;

    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @PostMapping("")
    public ResponseEntity<Suggestions> suggest(@Valid @RequestBody SuggestionRequest request) {
        return ResponseEntity.ok(suggestionService.suggestSubject(request));
    }


}
