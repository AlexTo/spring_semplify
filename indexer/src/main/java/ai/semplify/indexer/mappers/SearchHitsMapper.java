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
        var searchHitMapper = Mappers.getMapper(SearchHitMapper.class);
        var obj = new SearchHits();
        obj.setMaxScore(entity.getMaxScore());
        obj.setScrollId(entity.getScrollId());
        obj.setTotalHits(entity.getTotalHits());
        obj.setTotalHitsRelation(entity.getTotalHitsRelation());
        var searchHits = entity.getSearchHits();
        List<SearchHit> searchHitsModel = searchHits.stream().map(searchHitMapper::toModel).collect(Collectors.toList());
        obj.setSearchHits(searchHitsModel);
        return obj;
    }
}
