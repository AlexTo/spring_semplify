package ai.semplify.feignclients.clients.entityhub.models;

import lombok.Data;

@Data
public class AbstractResponse {
    private String text;
    private String predicate;
}
