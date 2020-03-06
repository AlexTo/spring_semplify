package ai.semplify.tasker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"ai.semplify.*"})
@EnableConfigurationProperties
@EnableEurekaClient
@EnableScheduling
public class TaskerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskerApplication.class, args);
    }

}
