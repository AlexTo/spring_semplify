package ai.semplify.indexer.models;

import lombok.Data;

@Data
public class SuggestionRequest {
    private String prefix;
    private String type;
}
