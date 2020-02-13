package ai.semplify.indexer.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class AnnotationClass {
    @Field(type = FieldType.Keyword)
    private String uri;

    @Field(type = FieldType.Keyword)
    private String label;
}
