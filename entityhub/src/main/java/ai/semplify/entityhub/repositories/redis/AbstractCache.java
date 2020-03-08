package ai.semplify.entityhub.repositories.redis;

import ai.semplify.entityhub.entities.redis.Abstract;
import org.springframework.data.repository.CrudRepository;

public interface AbstractCache extends CrudRepository<Abstract, String> {
}
