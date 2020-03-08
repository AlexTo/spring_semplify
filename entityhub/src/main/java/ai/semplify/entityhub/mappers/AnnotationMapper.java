package ai.semplify.entityhub.mappers;

import ai.semplify.commons.models.entityhub.Annotation;
import ai.semplify.feignclients.clients.spotlight.models.DBPediaAnnotation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {AnnotationResourceMapper.class})
public interface AnnotationMapper {
    Annotation toAnnotation(DBPediaAnnotation dbpediaAnnotation);

}
