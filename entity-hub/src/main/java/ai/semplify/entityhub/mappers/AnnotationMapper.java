package ai.semplify.entityhub.mappers;

import ai.semplify.entityhub.models.Annotation;
import ai.semplify.entityhub.models.spotlight.DBPediaAnnotation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {AnnotationResourceMapper.class})
public interface AnnotationMapper {
    Annotation toAnnotation(DBPediaAnnotation dbpediaAnnotation);

}
