package ai.semplify.indexer.mappers;

import ai.semplify.indexer.entities.elasticsearch.IndexedDocument;
import ai.semplify.indexer.models.HighlightField;
import ai.semplify.indexer.models.SearchHit;
import lombok.var;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SearchHitMapper {

    private DocumentMapper docMapper;

    @Autowired
    public final void setDocMapper(DocumentMapper docMapper) {
        this.docMapper = docMapper;
    }

    public SearchHit toModel(org.springframework.data.elasticsearch.core.SearchHit<IndexedDocument> entity) {

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
