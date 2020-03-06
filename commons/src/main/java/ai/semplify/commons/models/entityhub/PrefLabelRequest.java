package ai.semplify.commons.models.entityhub;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PrefLabelRequest {

    @NotEmpty
    private String uri;
}
