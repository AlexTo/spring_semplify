package ai.semplify.fileserver.services.impl;

import ai.semplify.fileserver.mappers.FileMapper;
import ai.semplify.fileserver.models.File;
import ai.semplify.fileserver.repos.FileRepository;
import ai.semplify.fileserver.services.FileService;
import ai.semplify.fileserver.services.URLResolver;
import lombok.var;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileServicePostgresImpl implements FileService {

    private FileRepository repo;
    private FileMapper mapper;
    private URLResolver urlResolver;

    public FileServicePostgresImpl(FileRepository repo,
                                   FileMapper mapper,
                                   URLResolver urlResolver) {
        this.repo = repo;
        this.mapper = mapper;
        this.urlResolver = urlResolver;
    }

    @Override
    public Mono<File> store(String fileName, String contentType, byte[] content) {

        var entity = new ai.semplify.fileserver.entities.File();
        entity.setName(fileName);
        entity.setContentType(contentType);
        entity.setContent(content);

        return Mono.just(mapper.toModel(repo.save(entity)));
    }

    @Override
    public Mono<File> store(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> {
                    try {
                        var content = StreamUtils.copyToByteArray(dataBuffer.asInputStream());
                        return store(filePart.filename(),
                                Objects.requireNonNull(filePart.headers().getContentType()).toString(),
                                content);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }

    @Override
    public Optional<File> findById(Long fileId) {
        return repo.findById(fileId).map(file -> {
            var model = mapper.toModel(file);
            model.setUrl(urlResolver.resolve(file.getId(), file.getModule()));
            return model;
        });
    }

    @Override
    public Optional<File> findInfoById(Long fileId) {
        return repo.findInfoById(fileId).map(file -> {
            var model = mapper.toModel(file);
            model.setUrl(urlResolver.resolve(file.getId(), file.getModule()));
            return model;
        });
    }

    @Override
    public List<File> findAllByModule(String module) {
        return repo.findAllByModule(module).stream().map(file -> {
            var model = mapper.toModel(file);
            model.setUrl(urlResolver.resolve(file.getId(), file.getModule()));
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public void deleteByIds(List<Long> ids) {
        var files = repo.findAllById(ids);
        repo.deleteInBatch(files);
    }
}
