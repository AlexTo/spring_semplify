package ai.semplify.indexer.models;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class SearchHit {
    private String id;
    private float score;
    private List<Object> sortValues;
    private Doc content;
    private Map<String, List<String>> highlightFields = new LinkedHashMap();
}
