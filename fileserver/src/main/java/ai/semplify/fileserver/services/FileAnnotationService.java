package ai.semplify.fileserver.services;

import ai.semplify.commons.models.fileserver.FileAnnotation;
import ai.semplify.commons.models.fileserver.FileAnnotationPage;
import ai.semplify.fileserver.exceptions.FileNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FileAnnotationService {

    FileAnnotation create(FileAnnotation fileAnnotation) throws FileNotFoundException;
    FileAnnotationPage findAll(Pageable pageable);
}
