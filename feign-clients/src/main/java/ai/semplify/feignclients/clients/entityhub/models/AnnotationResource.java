package ai.semplify.feignclients.clients.entityhub.models;

import lombok.Data;
import lombok.var;

import java.util.Objects;

@Data
public class AnnotationResource {

    private String uri;

    private String support;

    private String types;

    private String surfaceForm;

    private String prefLabel;

    private Integer offset;

    private Double similarityScore;

    private String percentageOfSecondRank;

    private String source;

    private String context;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var thatUri = ((AnnotationResource) o).getUri();
        return uri.equals(thatUri);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uri);
    }
}
