package ai.semplify.fileserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableConfigurationProperties
@EntityScan(basePackages = {"ai.semplify.commons.entities", "ai.semplify.fileserver.entities"})
public class FileServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

}
