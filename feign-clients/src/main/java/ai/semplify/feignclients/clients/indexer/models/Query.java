package ai.semplify.feignclients.clients.indexer.models;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class Query {
    @NotBlank
    private String q;
    @Min(0)
    private Integer page;
    @Max(100)
    private Integer size;
}
