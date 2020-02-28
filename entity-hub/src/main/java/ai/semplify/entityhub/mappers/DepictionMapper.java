package ai.semplify.entityhub.mappers;

import ai.semplify.entityhub.entities.redis.Depiction;
import ai.semplify.entityhub.models.DepictionResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DepictionMapper {

    DepictionResponse toModel(Depiction entity);

    @Mappings({
            @org.mapstruct.Mapping(target = "uri", ignore = true)
    })
    Depiction toEntity(DepictionResponse model);
}
