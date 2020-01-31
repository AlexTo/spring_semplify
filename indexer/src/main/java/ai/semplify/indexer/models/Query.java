package ai.semplify.indexer.models;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class Query {
    private String q;
    @Min(0)
    private Integer page;
    @Max(100)
    private Integer size;
}
