package ai.semplify.indexer.services;

import ai.semplify.indexer.models.SuggestionRequest;
import org.elasticsearch.action.search.SearchResponse;

public interface SuggestionService {
    SearchResponse suggestSubject(SuggestionRequest request);
}
