package ai.semplify.entityhub.mappers;

import ai.semplify.entityhub.models.AnnotationResource;
import ai.semplify.entityhub.models.spotlight.DBPediaAnnotationResource;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AnnotationResourceMapper {
    @Mappings({
            @Mapping(target = "source", ignore = true),
            @Mapping(target = "prefLabel", ignore = true)
    })
    AnnotationResource fromDBPediaAnnotationResource(DBPediaAnnotationResource dbpediaAnnotationResource);
}
