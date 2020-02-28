package ai.semplify.indexer.mappers;

import ai.semplify.indexer.models.DocumentMetadata;
import ai.semplify.indexer.models.Document;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {AnnotationMapper.class})
public interface DocumentMapper {

    Document toModel(ai.semplify.indexer.entities.elasticsearch.Document entity);

    @Mappings({
            @Mapping(target = "content", ignore = true),
            @Mapping(target = "annotations", ignore = true)
    })
    ai.semplify.indexer.entities.elasticsearch.Document toEntity(ai.semplify.indexer.entities.postgresql.Document document);

    @Mappings({
            @Mapping(target = "content", ignore = true),
            @Mapping(target = "contentType", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "error", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "type", ignore = true)
    })
    ai.semplify.indexer.entities.postgresql.Document toEntity(DocumentMetadata documentMetadata);

}
