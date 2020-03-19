package ai.semplify.fileserver.mappers;

import ai.semplify.commons.models.fileserver.FileAnnotationPage;
import ai.semplify.fileserver.entities.FileAnnotation;
import lombok.var;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AnnotationResourceMapper.class})
public interface FileAnnotationMapper {

    @Mappings({
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true)
    })
    FileAnnotation toEntity(ai.semplify.commons.models.fileserver.FileAnnotation fileAnnotation);

    @Mappings({
            @Mapping(target = "annotationResources", ignore = true)
    })
    ai.semplify.commons.models.fileserver.FileAnnotation toModel(FileAnnotation entity);

    default FileAnnotationPage toPage(Page<FileAnnotation> entity) {
        var fileAnnotationPage = new FileAnnotationPage();
        fileAnnotationPage.setTotalPages(entity.getTotalPages());
        fileAnnotationPage.setTotalElements(entity.getTotalElements());
        fileAnnotationPage.setHasNext(entity.hasNext());
        fileAnnotationPage.setHasPrevious(entity.hasPrevious());
        fileAnnotationPage.setIsEmpty(entity.isEmpty());
        fileAnnotationPage.setFileAnnotations(entity.get().map(this::toModel).collect(Collectors.toList()));
        return fileAnnotationPage;
    }
}
