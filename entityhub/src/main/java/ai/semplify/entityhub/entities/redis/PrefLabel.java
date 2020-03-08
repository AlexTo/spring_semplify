package ai.semplify.entityhub.entities.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("entity_hub_cache_pref_label")
public class PrefLabel {

    @Id
    private String uri;
    private String prefLabel;
    private String lang;
}
