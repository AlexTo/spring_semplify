package ai.semplify.indexer.mappers;

import ai.semplify.feignclients.clients.entityhub.EntityHubFeignClient;
import ai.semplify.feignclients.clients.entityhub.models.PrefLabelRequest;
import ai.semplify.indexer.entities.elasticsearch.Document;
import ai.semplify.indexer.models.Bucket;
import ai.semplify.indexer.models.SearchHit;
import ai.semplify.indexer.models.SearchHits;
import lombok.var;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedReverseNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SearchHitsMapper {

    private SearchHitMapper searchHitMapper;
    private EntityHubFeignClient entityHubFeignClient;

    @Autowired
    public final void setEntityHubFeignClient(EntityHubFeignClient entityHubFeignClient) {
        this.entityHubFeignClient = entityHubFeignClient;
    }

    @Autowired
    public final void setSearchHitMapper(SearchHitMapper searchHitMapper) {
        this.searchHitMapper = searchHitMapper;
    }

    public SearchHits toModel(org.springframework.data.elasticsearch.core.SearchHits<Document> entity) {
        var model = new SearchHits();
        model.setMaxScore(entity.getMaxScore());
        model.setScrollId(entity.getScrollId());
        model.setTotalHits(entity.getTotalHits());
        model.setTotalHitsRelation(entity.getTotalHitsRelation());
        var searchHits = entity.getSearchHits();
        List<SearchHit> searchHitsModel = searchHits.stream().map(searchHitMapper::toModel).collect(Collectors.toList());
        model.setSearchHits(searchHitsModel);

        var buckets = new ArrayList<Bucket>();
        var classLabelsBuckets = (ParsedStringTerms) ((ParsedNested) ((ParsedNested)
                Objects.requireNonNull(entity.getAggregations())
                        .asList().get(0)).getAggregations().asList().get(0)).getAggregations().asList().get(0);

        for (var b1 : classLabelsBuckets.getBuckets()) {
            var bucket = new Bucket();
            bucket.setUri(b1.getKeyAsString());
            bucket.setName(getPrefLabel(bucket.getUri()));
            bucket.setDocCount(b1.getDocCount());
            var annotationLabelsBuckets =
                    (ParsedStringTerms) ((ParsedReverseNested) b1.getAggregations()
                            .asList().get(0)).getAggregations().asList().get(0);
            var subBuckets = new ArrayList<Bucket>();

            for (var b2 : annotationLabelsBuckets.getBuckets()) {
                var subBucket = new Bucket();
                subBucket.setUri(b2.getKeyAsString());
                subBucket.setName(getPrefLabel(subBucket.getUri()));
                subBucket.setDocCount(b2.getDocCount());
                subBuckets.add(subBucket);
            }

            bucket.setBuckets(subBuckets);

            buckets.add(bucket);
        }

        model.setBuckets(buckets);
        return model;
    }

    private String getPrefLabel(String uri) {
        var prefLabelRequest = new PrefLabelRequest();
        prefLabelRequest.setUri(uri);
        var response = entityHubFeignClient.getPrefLabel(prefLabelRequest);
        return response.getPrefLabel();
    }
}
