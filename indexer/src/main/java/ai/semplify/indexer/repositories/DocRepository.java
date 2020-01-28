package ai.semplify.indexer.repositories;

import ai.semplify.indexer.entities.Doc;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocRepository extends ReactiveCrudRepository<Doc, String> {
}
