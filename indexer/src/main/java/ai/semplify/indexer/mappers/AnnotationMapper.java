package ai.semplify.indexer.mappers;

import ai.semplify.indexer.models.Annotation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {AnnotationClassMapper.class})
public interface AnnotationMapper {

    Annotation toModel(ai.semplify.indexer.entities.elasticsearch.Annotation entity);
}
