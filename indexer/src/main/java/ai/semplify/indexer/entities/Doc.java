package ai.semplify.indexer.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Document(indexName = "default")
public class Doc {

    @Id
    private String uri;

    private String label;

    private List<String> annotations;

    @Field(type = FieldType.Text)
    private String content;
}
