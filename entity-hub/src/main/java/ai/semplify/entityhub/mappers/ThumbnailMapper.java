package ai.semplify.entityhub.mappers;

import ai.semplify.entityhub.entities.redis.Thumbnail;
import ai.semplify.entityhub.models.ThumbnailResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ThumbnailMapper {

    ThumbnailResponse toModel(Thumbnail entity);

    @Mappings({
            @org.mapstruct.Mapping(target = "uri", ignore = true)
    })
    Thumbnail toEntity(ThumbnailResponse model);

}
