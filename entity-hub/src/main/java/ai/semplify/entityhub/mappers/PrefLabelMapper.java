package ai.semplify.entityhub.mappers;


import ai.semplify.entityhub.entities.redis.PrefLabel;
import ai.semplify.entityhub.models.PrefLabelResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PrefLabelMapper {

    PrefLabelResponse toModel(PrefLabel entity);

    @Mappings({
            @org.mapstruct.Mapping(target = "uri", ignore = true)
    })
    PrefLabel toEntity(PrefLabelResponse model);
}
