package ai.semplify.commons.models.entityhub;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UrlAnnotationRequest {
    @NotEmpty
    private String url;
}
