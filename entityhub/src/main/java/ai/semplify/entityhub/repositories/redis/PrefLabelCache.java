package ai.semplify.entityhub.repositories.redis;

import ai.semplify.entityhub.entities.redis.PrefLabel;
import org.springframework.data.repository.CrudRepository;

public interface PrefLabelCache extends CrudRepository<PrefLabel, String> {
}
