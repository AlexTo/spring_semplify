package ai.semplify.fileserver.mappers;

import ai.semplify.commons.entities.AnnotationResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnnotationResourceMapper {
    @Mappings({
            @Mapping(source = "offset", target = "_offset")
    })
    AnnotationResource toEntity(ai.semplify.commons.models.entityhub.AnnotationResource model);

    @Mappings({
            @Mapping(source = "_offset", target = "offset")
    })
    ai.semplify.commons.models.entityhub.AnnotationResource toModel(AnnotationResource entity);

    List<ai.semplify.commons.models.entityhub.AnnotationResource> toModels(List<AnnotationResource> entities);
}
