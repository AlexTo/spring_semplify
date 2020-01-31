package ai.semplify.feignclients.clients.indexer.models;

import lombok.Data;

import java.util.List;

@Data
public class SearchHits {
    private long totalHits;
    private String totalHitsRelation;
    private float maxScore;
    private String scrollId;
    private List<? extends SearchHit> searchHits;
}
