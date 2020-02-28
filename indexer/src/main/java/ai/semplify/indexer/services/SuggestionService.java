package ai.semplify.indexer.services;

import ai.semplify.indexer.models.SuggestionRequest;
import ai.semplify.indexer.models.Suggestions;

public interface SuggestionService {
    Suggestions suggestSubject(SuggestionRequest request);
}
