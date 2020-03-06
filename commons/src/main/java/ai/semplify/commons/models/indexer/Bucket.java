package ai.semplify.commons.models.indexer;

import lombok.Data;

import java.util.List;

@Data
public class Bucket {
    private String name;
    private String uri;
    private Long docCount;
    private List<Bucket> buckets;
}
