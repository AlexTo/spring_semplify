package ai.semplify.fileserver.repositories;

import ai.semplify.fileserver.entities.FileAnnotation;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAnnotationRepository extends JpaRepository<FileAnnotation, Long> {

}
