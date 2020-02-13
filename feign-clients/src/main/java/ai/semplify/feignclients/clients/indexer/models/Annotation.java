package ai.semplify.feignclients.clients.indexer.models;

import lombok.Data;

import java.util.Set;

@Data
public class Annotation {
    private String uri;
    private String label;
    private Set<AnnotationClass> classes;
}
