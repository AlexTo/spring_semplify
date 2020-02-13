package ai.semplify.feignclients.clients.entityhub;


import ai.semplify.feignclients.clients.entityhub.models.TypeCheckRequest;
import ai.semplify.feignclients.clients.entityhub.models.TypeCheckResponse;
import ai.semplify.feignclients.config.DefaultFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "entity-hub", configuration = DefaultFeignClientConfiguration.class)
public interface EntityHubFeignClient {

    @PostMapping("/entity/funcs/isSubClassOfTransitive")
    TypeCheckResponse isSubClassOfTransitive(@Valid @RequestBody TypeCheckRequest request);
}
