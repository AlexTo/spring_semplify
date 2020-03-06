package ai.semplify.commons.models.indexer;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchHit {
    private String id;
    private float score;
    private List<Object> sortValues;
    private Document content;
    private List<HighlightField> highlightFields = new ArrayList<>();
}
