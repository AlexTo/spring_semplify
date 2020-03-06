package ai.semplify.commons.models.spotlight;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DBPediaAnnotation {
    @JsonProperty("@text")
    private String text;

    @JsonProperty("@confidence")
    private String confidence;

    @JsonProperty("@support")
    private String support;

    @JsonProperty("@types")
    private String types;

    @JsonProperty("@sparql")
    private String sparql;

    @JsonProperty("@policy")
    private String policy;

    @JsonProperty("Resources")
    private List<DBPediaAnnotationResource> resources;
}
