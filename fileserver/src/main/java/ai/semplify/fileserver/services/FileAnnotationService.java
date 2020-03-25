package ai.semplify.fileserver.services;

import ai.semplify.commons.models.entityhub.AnnotationResource;
import ai.semplify.commons.models.fileserver.FileAnnotation;
import ai.semplify.commons.models.fileserver.FileAnnotationUpdate;
import ai.semplify.commons.models.fileserver.FileAnnotationPage;
import ai.semplify.fileserver.exceptions.FileAnnotationNotFoundException;
import ai.semplify.fileserver.exceptions.FileNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FileAnnotationService {

    FileAnnotation create(FileAnnotation fileAnnotation) throws FileNotFoundException;

    FileAnnotationPage findAll(Pageable pageable);

    FileAnnotation findById(Long id) throws FileNotFoundException;

    List<AnnotationResource> findAllAnnotationResources(Long fileAnnotationId) throws FileNotFoundException;

    FileAnnotation update(Long fileAnnotationId, FileAnnotationUpdate fileAnnotation) throws FileAnnotationNotFoundException;
}
