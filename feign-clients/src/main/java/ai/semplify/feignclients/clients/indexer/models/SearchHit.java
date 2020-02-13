package ai.semplify.feignclients.clients.indexer.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchHit {
    private String id;
    private float score;
    private List<Object> sortValues;
    private IndexedDocument content;
    private List<HighlightField> highlightFields = new ArrayList<>();
}
