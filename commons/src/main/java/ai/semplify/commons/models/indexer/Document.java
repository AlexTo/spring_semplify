package ai.semplify.commons.models.indexer;

import lombok.Data;

import java.util.Set;

@Data
public class Document {
    private String uri;
    private String label;
    private Set<Annotation> annotations;
}
