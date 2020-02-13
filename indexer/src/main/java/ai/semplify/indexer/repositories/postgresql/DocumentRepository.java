package ai.semplify.indexer.repositories.postgresql;

import ai.semplify.indexer.entities.postgresql.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findAllByStatusIsNullOrderByLastModifiedDateAsc(Pageable pageable);

}
