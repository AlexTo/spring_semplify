package ai.semplify.feignclients.clients.indexer.models;

import lombok.Data;

import java.util.List;

@Data
public class Bucket {
    private String name;
    private String uri;
    private Long docCount;
    private List<Bucket> buckets;
}
