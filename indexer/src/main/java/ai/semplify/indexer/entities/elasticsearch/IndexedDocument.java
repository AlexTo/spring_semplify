package ai.semplify.indexer.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.TermVector;

import java.util.Set;

@Data
@Document(indexName = "documents")
public class IndexedDocument {

    @Id
    private String uri;

    private String label;

    @Field(type = FieldType.Nested)
    private Set<Annotation> annotations;

    @Field(type = FieldType.Text, termVector = TermVector.with_offsets)
    private String content;
}
