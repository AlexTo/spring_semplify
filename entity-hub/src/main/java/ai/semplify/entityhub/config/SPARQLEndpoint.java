package ai.semplify.entityhub.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.sparql")
public class SPARQLEndpoint {
    private String url;
    private boolean auth;
    private String username;
    private String password;
}
