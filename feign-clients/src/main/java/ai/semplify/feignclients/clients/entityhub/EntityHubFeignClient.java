package ai.semplify.feignclients.clients.entityhub;


import ai.semplify.feignclients.clients.entityhub.models.*;
import ai.semplify.feignclients.config.DefaultFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "entity-hub", configuration = DefaultFeignClientConfiguration.class)
public interface EntityHubFeignClient {

    @PostMapping("/entity/funcs/isSubClassOf")
    TypeCheckResponse isSubClassOf(@Valid @RequestBody TypeCheckRequest request);

    @PostMapping("/entity/funcs/isNarrowerConceptOf")
    TypeCheckResponse isNarrowerConceptOf(@Valid @RequestBody TypeCheckRequest request);

    @PostMapping("/entity/funcs/getPrefLabel")
    PrefLabelResponse getPrefLabel(@Valid @RequestBody PrefLabelRequest request);

    @PostMapping("/entity/funcs/getDepiction")
    DepictionResponse getDepiction(@Valid @RequestBody DepictionRequest request);
}
