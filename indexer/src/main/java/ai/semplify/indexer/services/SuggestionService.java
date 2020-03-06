package ai.semplify.indexer.services;

import ai.semplify.commons.models.indexer.SuggestionRequest;
import ai.semplify.commons.models.indexer.Suggestions;

public interface SuggestionService {
    Suggestions suggestSubject(SuggestionRequest request);
}
