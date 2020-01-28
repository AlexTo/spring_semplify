package ai.semplify.indexer.mappers;

import ai.semplify.indexer.models.Doc;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DocMapper {

    Doc toModel(ai.semplify.indexer.entities.Doc entity);

    @Mappings({
            @org.mapstruct.Mapping(target = "content", ignore = true)
    })
    ai.semplify.indexer.entities.Doc toEntity(Doc model);
}
