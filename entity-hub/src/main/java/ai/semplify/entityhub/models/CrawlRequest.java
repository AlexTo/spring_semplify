package ai.semplify.entityhub.models;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CrawlRequest {
    @NotEmpty
    @URL
    private String url;

    @NotNull
    private Integer depth;

}
