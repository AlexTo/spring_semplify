package ai.semplify.feignclients.clients.entityhub;


import ai.semplify.feignclients.config.DefaultFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "entity-hub", configuration = DefaultFeignClientConfiguration.class)
public interface EntityHubFeignClient {

    @GetMapping("/ner/test")
    String test();
}
