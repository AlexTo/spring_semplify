package ai.semplify.indexer.models;

import lombok.Data;

import java.util.Set;

@Data
public class IndexedDocument {
    private String uri;
    private String label;
    private Set<Annotation> annotations;
}
