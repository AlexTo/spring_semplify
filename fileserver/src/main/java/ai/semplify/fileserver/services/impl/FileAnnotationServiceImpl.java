package ai.semplify.fileserver.services.impl;

import ai.semplify.commons.models.entityhub.AnnotationResource;
import ai.semplify.commons.models.fileserver.FileAnnotation;
import ai.semplify.commons.models.fileserver.FileAnnotationPage;
import ai.semplify.commons.models.fileserver.FileAnnotationStatus;
import ai.semplify.fileserver.exceptions.FileAnnotationNotFoundException;
import ai.semplify.fileserver.exceptions.FileNotFoundException;
import ai.semplify.fileserver.mappers.AnnotationResourceMapper;
import ai.semplify.fileserver.mappers.FileAnnotationMapper;
import ai.semplify.fileserver.repositories.AnnotationResourceRepository;
import ai.semplify.fileserver.repositories.FileAnnotationRepository;
import ai.semplify.fileserver.repositories.FileRepository;
import ai.semplify.fileserver.services.FileAnnotationService;
import lombok.var;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileAnnotationServiceImpl implements FileAnnotationService {

    private FileRepository fileRepository;
    private FileAnnotationRepository fileAnnotationRepository;
    private FileAnnotationMapper fileAnnotationMapper;
    private AnnotationResourceMapper annotationResourceMapper;

    private AnnotationResourceRepository annotationResourceRepository;

    public FileAnnotationServiceImpl(FileRepository fileRepository,
                                     FileAnnotationRepository fileAnnotationRepository,
                                     FileAnnotationMapper fileAnnotationMapper,
                                     AnnotationResourceMapper annotationResourceMapper,
                                     AnnotationResourceRepository annotationResourceRepository) {
        this.fileRepository = fileRepository;
        this.fileAnnotationRepository = fileAnnotationRepository;
        this.fileAnnotationMapper = fileAnnotationMapper;
        this.annotationResourceMapper = annotationResourceMapper;
        this.annotationResourceRepository = annotationResourceRepository;
    }

    @Override
    public FileAnnotation create(FileAnnotation fileAnnotation) throws FileNotFoundException {
        var file = fileRepository.findById(fileAnnotation.getFile().getId())
                .orElseThrow(FileNotFoundException::new);

        var entity = fileAnnotationMapper.toEntity(fileAnnotation);
        entity.setFile(file);
        entity.setStatus(FileAnnotationStatus.PENDING_REVIEW);
        if (entity.getAnnotationResources() != null)
            entity.setAnnotationResources(annotationResourceRepository.saveAll(entity.getAnnotationResources()));
        return fileAnnotationMapper.toModel(fileAnnotationRepository.save(entity));
    }

    @Override
    public FileAnnotationPage findAll(Pageable pageable) {
        return fileAnnotationMapper
                .toPage(fileAnnotationRepository.findAll(pageable));
    }

    @Override
    public FileAnnotation findById(Long id) throws FileNotFoundException {
        var entity = fileAnnotationRepository.findById(id).orElseThrow(FileNotFoundException::new);
        return fileAnnotationMapper.toModel(entity);
    }

    @Override
    public List<AnnotationResource> findAllAnnotationResources(Long fileAnnotationId) throws FileNotFoundException {
        var fileAnnotation = fileAnnotationRepository.findById(fileAnnotationId).orElseThrow(FileNotFoundException::new);
        return annotationResourceMapper.toModels(fileAnnotation.getAnnotationResources());
    }

    @Override
    public FileAnnotation update(Long fileAnnotationId, FileAnnotation fileAnnotation) throws FileAnnotationNotFoundException {
        var entity = fileAnnotationRepository.findById(fileAnnotationId).orElseThrow(FileAnnotationNotFoundException::new);

        if (entity.getStatus().equalsIgnoreCase(FileAnnotationStatus.PENDING_REVIEW)) {
            return fileAnnotationMapper.toModel(entity);
        }

        if (fileAnnotation == null || !fileAnnotation.getStatus().equalsIgnoreCase(FileAnnotationStatus.REVIEWED)) {
            return fileAnnotationMapper.toModel(entity);
        }

        var keptAnnotationResourceIds = fileAnnotation.getAnnotationResources() == null ? Collections.emptyList() :
                fileAnnotation.getAnnotationResources()
                        .stream()
                        .filter(a -> a.getId() != null)
                        .map(AnnotationResource::getId)
                        .collect(Collectors.toList());
        var keptAnnotationResources = entity.getAnnotationResources()
                .stream()
                .filter(a -> keptAnnotationResourceIds.contains(a.getId()))
                .collect(Collectors.toList());

        var removedAnnotationResources = entity.getAnnotationResources()
                .stream()
                .filter(a -> !keptAnnotationResourceIds.contains(a.getId()))
                .collect(Collectors.toList());

        annotationResourceRepository.deleteAll(removedAnnotationResources);

        var newAnnotationResources = fileAnnotation.getAnnotationResources() == null
                ? new ArrayList<ai.semplify.commons.entities.AnnotationResource>()
                : fileAnnotation.getAnnotationResources()
                .stream()
                .filter(a -> a.getId() == null)
                .map(a -> annotationResourceMapper.toEntity(a))
                .collect(Collectors.toList());

        newAnnotationResources = annotationResourceRepository.saveAll(newAnnotationResources);

        newAnnotationResources.addAll(keptAnnotationResources);

        entity.setAnnotationResources(newAnnotationResources);

        return fileAnnotationMapper.toModel(entity);
    }
}
