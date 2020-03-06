package ai.semplify.fileserver.services;

import ai.semplify.commons.models.fileserver.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileService {

    File store(String fileName, String contentType, byte[] content) throws IOException;

    File store(MultipartFile filePart) throws IOException;

    Optional<File> findById(Long fileId);

    Optional<File> findInfoById(Long fileId);

    List<File> findAllByModule(String module);

    void deleteById(Long fileId);

    void deleteByIds(List<Long> ids);
}
