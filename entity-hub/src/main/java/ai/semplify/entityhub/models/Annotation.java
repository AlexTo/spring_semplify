package ai.semplify.entityhub.models;

import lombok.Data;

import java.util.List;

@Data
public class Annotation {

    private String text;

    private String confidence;

    private String support;

    private String types;

    private String sparql;

    private String policy;

    private List<AnnotationResource> resources;
}
