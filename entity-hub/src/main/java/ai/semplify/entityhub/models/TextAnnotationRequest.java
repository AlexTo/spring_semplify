package ai.semplify.entityhub.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TextAnnotationRequest {
    @NotNull
    private String text;
}
