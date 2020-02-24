package ai.semplify.entityhub.entities.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("entity_hub_cache_depiction")
public class Depiction {
    @Id
    private String uri;
    private String depictionUri;
}
