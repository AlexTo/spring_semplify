package ai.semplify.entityhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "ai.semplify.*")
@EnableDiscoveryClient
@EnableConfigurationProperties
public class EntityHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntityHubApplication.class, args);
    }
}
