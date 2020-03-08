package ai.semplify.feignclients.clients.entityhub.models;

import lombok.Data;

@Data
public class EntitySummaryResponse {
    private PrefLabelResponse prefLabel;
    private ThumbnailResponse thumbnail;
    private AbstractResponse abstract_;
}
