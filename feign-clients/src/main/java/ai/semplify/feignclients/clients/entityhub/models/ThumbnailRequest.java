package ai.semplify.feignclients.clients.entityhub.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ThumbnailRequest {
    @NotEmpty
    private String uri;
}
