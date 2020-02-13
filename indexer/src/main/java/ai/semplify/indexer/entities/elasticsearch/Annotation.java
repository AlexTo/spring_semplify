package ai.semplify.indexer.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

@Data
public class Annotation {

    @Field(type = FieldType.Keyword)
    private String uri;

    @Field(type = FieldType.Keyword)
    private String label;

    @Field(type = FieldType.Nested)
    private Set<AnnotationClass> classes;
}
