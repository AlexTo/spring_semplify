package ai.semplify.commons.models.indexer;

import lombok.Data;

import java.util.Set;

@Data
public class DocumentMetadata {

    private String uri;

    private String label;

    private Set<String> annotations;
}
