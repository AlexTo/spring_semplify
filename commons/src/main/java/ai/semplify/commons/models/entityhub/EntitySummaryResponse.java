package ai.semplify.commons.models.entityhub;

import lombok.Data;

@Data
public class EntitySummaryResponse {
    private PrefLabelResponse prefLabel;
    private ThumbnailResponse thumbnail;
    private AbstractResponse abstract_;
}
