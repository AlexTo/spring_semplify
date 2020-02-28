package ai.semplify.entityhub.mappers;

import ai.semplify.entityhub.entities.redis.Abstract;
import ai.semplify.entityhub.models.AbstractResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AbstractMapper {
    @Mappings({

            @org.mapstruct.Mapping(target = "predicate", ignore = true)
    })
    AbstractResponse toModel(Abstract entity);

    @Mappings({

            @org.mapstruct.Mapping(target = "uri", ignore = true)
    })
    Abstract toEntity(AbstractResponse model);
}
