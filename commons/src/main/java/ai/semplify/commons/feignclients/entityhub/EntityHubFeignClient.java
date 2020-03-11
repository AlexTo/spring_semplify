package ai.semplify.commons.feignclients.entityhub;


import ai.semplify.commons.feignclients.DefaultFeignClientConfiguration;
import ai.semplify.commons.models.entityhub.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/entity/funcs/getThumbnail")
    ThumbnailResponse getThumbnail(@Valid @RequestBody ThumbnailRequest request);

    @PostMapping("/entity/funcs/getSummary")
    EntitySummaryResponse getSummary(@Valid @RequestBody EntitySummaryRequest request);

    @PostMapping("/entity/funcs/getAbstract")
    AbstractResponse getAbstract(@Valid @RequestBody AbstractRequest request);

    @PostMapping("ner/serverFile")
    Annotation annotateServerFile(@RequestBody Long fileId);


}
