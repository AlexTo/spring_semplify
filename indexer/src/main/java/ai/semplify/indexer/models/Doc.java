package ai.semplify.indexer.models;

import lombok.Data;

import java.util.List;

@Data
public class Doc {
    private String uri;
    private String title;
    private List<String> tag;
}
