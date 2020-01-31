package ai.semplify.indexer.mappers;

import ai.semplify.indexer.models.SearchHit;
import ai.semplify.indexer.models.SearchHits;
import lombok.var;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SearchHitsMapper {

    default SearchHits toModel(org.springframework.data.elasticsearch.core.SearchHits<ai.semplify.indexer.entities.Doc> entity) {

        var docMapper = Mappers.getMapper(DocMapper.class);

        var obj = new SearchHits();
        obj.setMaxScore(entity.getMaxScore());
        obj.setScrollId(entity.getScrollId());
        obj.setTotalHits(entity.getTotalHits());
        obj.setTotalHitsRelation(entity.getTotalHitsRelation());
        var searchHits = entity.getSearchHits();
        List<SearchHit> searchHitsModel = searchHits.stream().map(s -> {
            var searchHit = new SearchHit();
            searchHit.setId(s.getId());
            searchHit.setHighlightFields(s.getHighlightFields());
            searchHit.setScore(s.getScore());
            searchHit.setSortValues(s.getSortValues());
            searchHit.setContent(docMapper.toModel(s.getContent()));
            return searchHit;
        }).collect(Collectors.toList());

        obj.setSearchHits(searchHitsModel);

        return obj;
    }
}
