package ai.semplify.indexer.mappers;

import ai.semplify.commons.models.indexer.Suggestion;
import lombok.var;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SuggestionMapper {

    public Suggestion toModel(CompletionSuggestion.Entry.Option entity) {
        var suggestion = new Suggestion();
        suggestion.setUri(entity.getHit().getId());
        suggestion.setText(entity.getText().string());
        return suggestion;
    }
}
