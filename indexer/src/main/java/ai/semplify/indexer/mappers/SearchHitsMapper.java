package ai.semplify.indexer.mappers;

import ai.semplify.indexer.entities.elasticsearch.IndexedDocument;
import ai.semplify.indexer.models.SearchHit;
import ai.semplify.indexer.models.SearchHits;
import lombok.var;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SearchHitsMapper {

    private SearchHitMapper searchHitMapper;

    @Autowired
    public final void setSearchHitMapper(SearchHitMapper searchHitMapper) {
        this.searchHitMapper = searchHitMapper;
    }

    public SearchHits toModel(org.springframework.data.elasticsearch.core.SearchHits<IndexedDocument> entity) {
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
