package ai.semplify.commons.models.indexer;

import lombok.Data;

import java.util.List;

@Data
public class HighlightField {
    private String fieldName;
    private List<String> highlights;
}
