package ai.semplify.entityhub.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.proxy")
public class ProxyConfig {
    private String host;
    private Integer port;
}
