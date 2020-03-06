package ai.semplify.commons.models.entityhub;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CrawlRequest {
    @NotEmpty
    private String url;

    @NotNull
    private Integer depth;

}
