package ai.semplify.entityhub.mappers;

import ai.semplify.commons.models.entityhub.AnnotationResource;
import ai.semplify.commons.models.spotlight.DBPediaAnnotationResource;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AnnotationResourceMapper {
    @Mappings({
            @Mapping(target = "source", ignore = true),
            @Mapping(target = "prefLabel", ignore = true),
            @Mapping(target = "context", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    AnnotationResource fromDBPediaAnnotationResource(DBPediaAnnotationResource dbpediaAnnotationResource);
}
