package ai.semplify.entityhub.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TextAnnotationRequest {
    @NotEmpty
    private String text;
}
