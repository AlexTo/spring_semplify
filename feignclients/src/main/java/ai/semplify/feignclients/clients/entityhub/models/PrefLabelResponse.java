package ai.semplify.feignclients.clients.entityhub.models;

import lombok.Data;

@Data
public class PrefLabelResponse {
    private String prefLabel;
    private String lang;
    private String predicate;
}
