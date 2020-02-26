package ai.semplify.entityhub.entities.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("entity_hub_cache_thumbnail")
public class Thumbnail {
    @Id
    private String uri;
    private String thumbnailUri;
}
