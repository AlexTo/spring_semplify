package ai.semplify.fileserver.repos;

import ai.semplify.fileserver.entities.File;
import ai.semplify.fileserver.entities.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<FileInfo> findInfoById(Long id);

    Optional<File> findById(Long id);

    List<FileInfo> findAllByModule(String module);
}
