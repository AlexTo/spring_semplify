package ai.semplify.fileserver.mappers;

import ai.semplify.commons.models.fileserver.File;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FileMapper {

    @Mappings({
            @Mapping(target = "url", ignore = true),
    })
    File toModel(ai.semplify.fileserver.entities.File entity);

    ai.semplify.fileserver.entities.File toEntity(File file);

    @Mappings({
            @Mapping(target = "content", ignore = true),
            @Mapping(target = "url", ignore = true)
    })
    File toModel(ai.semplify.fileserver.entities.FileInfo entity);
}
