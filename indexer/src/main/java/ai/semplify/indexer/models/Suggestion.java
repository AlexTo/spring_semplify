package ai.semplify.indexer.models;

import lombok.Data;

@Data
public class Suggestion {
    private String uri;
    private String prefLabel;
    private String thumbnailUri;
    private String text;
}
