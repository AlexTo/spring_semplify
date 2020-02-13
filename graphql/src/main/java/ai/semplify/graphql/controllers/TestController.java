package ai.semplify.graphql.controllers;

import ai.semplify.feignclients.clients.entityhub.EntityHubFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    private EntityHubFeignClient entityHubFeignClient;

    public TestController(EntityHubFeignClient entityHubFeignClient) {
        this.entityHubFeignClient = entityHubFeignClient;
    }
}
