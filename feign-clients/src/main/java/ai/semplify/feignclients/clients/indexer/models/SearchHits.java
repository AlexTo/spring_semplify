package ai.semplify.feignclients.clients.indexer.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchHits {
    private long totalHits;
    private String totalHitsRelation;
    private float maxScore;
    private String scrollId;
    private List<? extends SearchHit> searchHits;
    private List<Bucket> buckets;
}
