package ai.semplify.entityhub.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EntitySummaryResponse {
    private PrefLabelResponse prefLabel;
    private ThumbnailResponse thumbnail;
    private AbstractResponse abstract_;
}
