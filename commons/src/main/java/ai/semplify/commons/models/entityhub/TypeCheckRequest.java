package ai.semplify.commons.models.entityhub;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TypeCheckRequest {
    @NotEmpty
    private String entity;

    @NotEmpty
    private String type;

    private Boolean transitive;
}
