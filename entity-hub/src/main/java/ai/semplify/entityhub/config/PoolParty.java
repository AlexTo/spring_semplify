package ai.semplify.entityhub.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "poolparty")
public class PoolParty {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String projectId;
}
