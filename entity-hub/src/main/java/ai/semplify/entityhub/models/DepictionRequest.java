package ai.semplify.entityhub.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DepictionRequest {
    @NotEmpty
    private String uri;
}
