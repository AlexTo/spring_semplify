package ai.semplify.indexer.models;

import lombok.Data;

import java.util.List;

@Data
public class HighlightField {
    private String fieldName;
    private List<String> highlights;
}
