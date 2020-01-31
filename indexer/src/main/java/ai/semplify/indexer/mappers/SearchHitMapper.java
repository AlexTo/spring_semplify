package ai.semplify.indexer.mappers;

import ai.semplify.indexer.entities.Doc;
import ai.semplify.indexer.models.HighlightField;
import ai.semplify.indexer.models.SearchHit;
import lombok.var;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SearchHitMapper {

    default SearchHit toModel(org.springframework.data.elasticsearch.core.SearchHit<Doc> entity) {
        var docMapper = Mappers.getMapper(DocMapper.class);

        var searchHit = new SearchHit();
        searchHit.setId(entity.getId());

        var highlightFields = entity.getHighlightFields().entrySet().stream().map(e -> {
            HighlightField highlightField = new HighlightField();
            highlightField.setFieldName(e.getKey());
            highlightField.setHighlights(e.getValue());
            return highlightField;
        }).collect(Collectors.toList());

        searchHit.setHighlightFields(highlightFields);
        searchHit.setScore(entity.getScore());
        searchHit.setSortValues(entity.getSortValues());
        searchHit.setContent(docMapper.toModel(entity.getContent()));
        return searchHit;
    }
}
