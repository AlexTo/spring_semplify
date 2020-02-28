package ai.semplify.indexer.services.impl;

import ai.semplify.indexer.models.SuggestionRequest;
import ai.semplify.indexer.services.SuggestionService;
import lombok.var;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    private ElasticsearchOperations elasticsearchOperations;
    private IndexCoordinates subjectsIndex;

    public SuggestionServiceImpl(ElasticsearchOperations elasticsearchOperations,
                                 @Qualifier("subjects_index") IndexCoordinates subjectsIndex) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.subjectsIndex = subjectsIndex;
    }

    @Override
    public SearchResponse suggestSubject(SuggestionRequest request) {

        var suggestionBuilder = SuggestBuilders
                .completionSuggestion("completion")
                .prefix(request.getPrefix(), Fuzziness.AUTO);

        var searchResponse = elasticsearchOperations
                .suggest(new SuggestBuilder()
                        .addSuggestion("suggestion", suggestionBuilder), subjectsIndex);
        return searchResponse;
    }
}
