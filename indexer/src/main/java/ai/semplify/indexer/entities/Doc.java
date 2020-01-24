package ai.semplify.indexer.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@Document(indexName = "default")
public class Doc {

    @Id
    private String uri;

    private String title;

    private List<String> annotations;
}
