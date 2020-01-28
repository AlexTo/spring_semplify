package ai.semplify.indexer.models;

import lombok.Data;

import java.util.List;

@Data
public class Doc {
    private String uri;
    private String label;
    private List<String> annotations;
}
