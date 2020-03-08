package ai.semplify.entityhub.repositories.redis;

import ai.semplify.entityhub.entities.redis.Depiction;
import org.springframework.data.repository.CrudRepository;

public interface DepictionCache extends CrudRepository<Depiction, String> {
}
