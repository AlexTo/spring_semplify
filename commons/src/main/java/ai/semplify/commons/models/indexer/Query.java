package ai.semplify.commons.models.indexer;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Data
public class Query {
    private String q;

    private List<String> selectedAnnotations;

    @Min(0)
    private Integer page;
    @Max(100)
    private Integer size;
}
