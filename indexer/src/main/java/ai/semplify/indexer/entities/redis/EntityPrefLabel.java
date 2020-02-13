package ai.semplify.indexer.entities.redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Data
@RedisHash
public class EntityPrefLabel {
    @Id
    private String uri;
    private String label;
}
