package ai.semplify.indexer.mappers;

import ai.semplify.commons.models.indexer.AnnotationClass;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AnnotationClassMapper {

    AnnotationClass toModel(ai.semplify.indexer.entities.elasticsearch.AnnotationClass entity);
}
