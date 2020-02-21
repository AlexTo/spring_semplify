package ai.semplify.entityhub.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DepictionResponse {
    @NotEmpty
    private String uri;
}
