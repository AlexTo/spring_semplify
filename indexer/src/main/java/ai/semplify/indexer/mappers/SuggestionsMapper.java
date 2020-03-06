package ai.semplify.indexer.mappers;

import ai.semplify.commons.models.indexer.Suggestions;
import lombok.var;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SuggestionsMapper {

    private SuggestionMapper suggestionMapper;

    @Autowired
    public void setSuggestionMapper(SuggestionMapper suggestionMapper) {
        this.suggestionMapper = suggestionMapper;
    }

    public Suggestions toModel(SearchResponse entity) {
        var model = new Suggestions();
        var suggestions = StreamSupport.stream(entity.getSuggest().spliterator(), false).collect(Collectors.toList());
        CompletionSuggestion suggestion = (CompletionSuggestion) suggestions.get(0);
        CompletionSuggestion.Entry entry = suggestion.getEntries().get(0);
        var options = entry.getOptions();
        model.setSuggestions(options.stream().map(o -> suggestionMapper.toModel(o)).collect(Collectors.toList()));
        return model;
    }
}
