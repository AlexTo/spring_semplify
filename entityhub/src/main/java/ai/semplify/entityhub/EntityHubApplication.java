package ai.semplify.entityhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "ai.semplify.*")
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableFeignClients(basePackages = "ai.semplify.commons.feignclients.*")
public class EntityHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntityHubApplication.class, args);
    }
}
