package ai.semplify.indexer.services.impl;

import ai.semplify.commons.feignclients.entityhub.EntityHubFeignClient;
import ai.semplify.commons.models.entityhub.PrefLabelRequest;
import ai.semplify.commons.models.entityhub.ThumbnailRequest;
import ai.semplify.indexer.mappers.SuggestionsMapper;
import ai.semplify.commons.models.indexer.SuggestionRequest;
import ai.semplify.commons.models.indexer.Suggestions;
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
    private EntityHubFeignClient entityHubFeignClient;

    public SuggestionServiceImpl(SuggestionsMapper suggestionsMapper,
                                 ElasticsearchOperations elasticsearchOperations,
                                 @Qualifier("subjects_index") IndexCoordinates subjectsIndex,
                                 EntityHubFeignClient entityHubFeignClient) {
        this.suggestionsMapper = suggestionsMapper;
        this.elasticsearchOperations = elasticsearchOperations;
        this.subjectsIndex = subjectsIndex;
        this.entityHubFeignClient = entityHubFeignClient;
    }

    @Override
    public Suggestions suggestSubject(SuggestionRequest request) {

        var suggestionBuilder = SuggestBuilders
                .completionSuggestion("completion")
                .text(request.getText());

        var searchResponse = elasticsearchOperations
                .suggest(new SuggestBuilder()
                        .addSuggestion("suggestion", suggestionBuilder), subjectsIndex);

        var suggestions = suggestionsMapper.toModel(searchResponse);
        for (var suggestion : suggestions.getSuggestions()) {
            var prefLabelRequest = new PrefLabelRequest();
            prefLabelRequest.setUri(suggestion.getUri());
            var prefLabel = entityHubFeignClient.getPrefLabel(prefLabelRequest);
            suggestion.setPrefLabel(prefLabel.getPrefLabel());
            var thumbnailRequest = new ThumbnailRequest();
            thumbnailRequest.setUri(suggestion.getUri());
            var thumbnail = entityHubFeignClient.getThumbnail(thumbnailRequest);
            suggestion.setThumbnailUri(thumbnail.getThumbnailUri());
        }
        return suggestions;

    }
}
