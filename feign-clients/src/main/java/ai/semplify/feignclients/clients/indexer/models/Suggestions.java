package ai.semplify.feignclients.clients.indexer.models;

import lombok.Data;

import java.util.List;

@Data
public class Suggestions {
    private List<Suggestion> suggestions;
}
