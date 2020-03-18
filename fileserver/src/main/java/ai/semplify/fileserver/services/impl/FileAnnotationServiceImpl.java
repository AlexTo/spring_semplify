package ai.semplify.fileserver.services.impl;

import ai.semplify.commons.models.fileserver.FileAnnotation;
import ai.semplify.commons.models.fileserver.FileAnnotationPage;
import ai.semplify.fileserver.exceptions.FileNotFoundException;
import ai.semplify.fileserver.mappers.FileAnnotationMapper;
import ai.semplify.fileserver.repositories.AnnotationResourceRepository;
import ai.semplify.fileserver.repositories.FileAnnotationRepository;
import ai.semplify.fileserver.repositories.FileRepository;
import ai.semplify.fileserver.services.FileAnnotationService;
import lombok.var;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FileAnnotationServiceImpl implements FileAnnotationService {

    private FileRepository fileRepository;
    private FileAnnotationRepository fileAnnotationRepository;
    private FileAnnotationMapper fileAnnotationMapper;
    private AnnotationResourceRepository annotationResourceRepository;

    public FileAnnotationServiceImpl(FileRepository fileRepository,
                                     FileAnnotationRepository fileAnnotationRepository,
                                     FileAnnotationMapper fileAnnotationMapper,
                                     AnnotationResourceRepository annotationResourceRepository) {
        this.fileRepository = fileRepository;
        this.fileAnnotationRepository = fileAnnotationRepository;
        this.fileAnnotationMapper = fileAnnotationMapper;
        this.annotationResourceRepository = annotationResourceRepository;
    }

    @Override
    public FileAnnotation create(FileAnnotation fileAnnotation) throws FileNotFoundException {
        var file = fileRepository.findById(fileAnnotation.getFile().getId())
                .orElseThrow(FileNotFoundException::new);

        var entity = fileAnnotationMapper.toEntity(fileAnnotation);
        entity.setFile(file);
        if (entity.getAnnotationResources() != null)
            entity.setAnnotationResources(annotationResourceRepository.saveAll(entity.getAnnotationResources()));
        return fileAnnotationMapper.toModel(fileAnnotationRepository.save(entity));
    }

    @Override
    public FileAnnotationPage findAll(Pageable pageable) {
        return fileAnnotationMapper
                .toPage(fileAnnotationRepository.findAll(pageable));
    }
}
