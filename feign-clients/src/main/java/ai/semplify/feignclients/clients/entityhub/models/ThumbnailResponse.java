package ai.semplify.feignclients.clients.entityhub.models;

import lombok.Data;

@Data
public class ThumbnailResponse {
    private String thumbnailUri;
    private String predicate;
}
