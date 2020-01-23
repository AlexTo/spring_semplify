package ai.semplify.fileserver.services;

import ai.semplify.fileserver.models.File;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileService {

    Mono<File> store(String fileName, String contentType, byte[] content) throws IOException;

    Mono<File> store(FilePart filePart);

    Optional<File> findById(Long fileId);

    Optional<File> findInfoById(Long fileId);

    List<File> findAllByModule(String module);

    void deleteById(Long fileId);

    void deleteByIds(List<Long> ids);
}
