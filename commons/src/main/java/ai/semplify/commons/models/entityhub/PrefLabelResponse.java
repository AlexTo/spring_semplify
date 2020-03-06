package ai.semplify.commons.models.entityhub;

import lombok.Data;

@Data
public class PrefLabelResponse {
    private String prefLabel;
    private String lang;
    private String predicate;
}
