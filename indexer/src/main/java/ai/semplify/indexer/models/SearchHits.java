package ai.semplify.indexer.models;

import lombok.Data;

import java.util.List;

@Data
public class SearchHits {
    private long totalHits;
    private org.springframework.data.elasticsearch.core.SearchHits.TotalHitsRelation totalHitsRelation;
    private float maxScore;
    private String scrollId;
    private List<? extends SearchHit> searchHits;
}
