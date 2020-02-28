package ai.semplify.indexer.services.impl;

import ai.semplify.indexer.mappers.SuggestionsMapper;
import ai.semplify.indexer.models.SuggestionRequest;
import ai.semplify.indexer.models.Suggestions;
import ai.semplify.indexer.services.SuggestionService;
import lombok.var;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    private SuggestionsMapper suggestionsMapper;
    private ElasticsearchOperations elasticsearchOperations;
    private IndexCoordinates subjectsIndex;

    public SuggestionServiceImpl(SuggestionsMapper suggestionsMapper,
                                 ElasticsearchOperations elasticsearchOperations,
                                 @Qualifier("subjects_index") IndexCoordinates subjectsIndex) {
        this.suggestionsMapper = suggestionsMapper;
        this.elasticsearchOperations = elasticsearchOperations;
        this.subjectsIndex = subjectsIndex;
    }

    @Override
    public Suggestions suggestSubject(SuggestionRequest request) {

        var suggestionBuilder = SuggestBuilders
                .completionSuggestion("completion")
                .text(request.getText());

        var searchResponse = elasticsearchOperations
                .suggest(new SuggestBuilder()
                        .addSuggestion("suggestion", suggestionBuilder), subjectsIndex);
        return suggestionsMapper.toModel(searchResponse);

    }
}
