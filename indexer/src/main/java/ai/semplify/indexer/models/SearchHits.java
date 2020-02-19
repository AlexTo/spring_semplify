package ai.semplify.indexer.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchHits {
    private long totalHits;
    private org.springframework.data.elasticsearch.core.SearchHits.TotalHitsRelation totalHitsRelation;
    private float maxScore;
    private String scrollId;
    private List<? extends SearchHit> searchHits;
    private List<Bucket> buckets;
}
