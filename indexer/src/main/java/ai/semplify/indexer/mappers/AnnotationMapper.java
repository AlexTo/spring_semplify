package ai.semplify.indexer.mappers;

import ai.semplify.commons.models.indexer.Annotation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {AnnotationClassMapper.class})
public interface AnnotationMapper {

    Annotation toModel(ai.semplify.indexer.entities.elasticsearch.Annotation entity);
}
