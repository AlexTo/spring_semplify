package ai.semplify.indexer.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.completion.Completion;

@Data
@org.springframework.data.elasticsearch.annotations.Document(indexName = "subjects")
public class Subject {

    @Id
    private String uri;

    @Field(type = FieldType.Text)
    private String prefLabel;

    @CompletionField(maxInputLength = 100)
    private Completion completion;
}
