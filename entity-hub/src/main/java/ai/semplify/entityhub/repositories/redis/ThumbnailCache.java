package ai.semplify.entityhub.repositories.redis;

import ai.semplify.entityhub.entities.redis.Thumbnail;
import org.springframework.data.repository.CrudRepository;

public interface ThumbnailCache extends CrudRepository<Thumbnail, String> {
}
