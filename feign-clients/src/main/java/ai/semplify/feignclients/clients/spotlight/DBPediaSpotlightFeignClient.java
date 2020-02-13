package ai.semplify.feignclients.clients.spotlight;

import ai.semplify.feignclients.clients.spotlight.models.DBPediaAnnotation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(value = "spotlight",
        url = "${spotlight.host}:${spotlight.port}",
        configuration = DBPediaFeignClientConfiguration.class)
public interface DBPediaSpotlightFeignClient {

    @PostMapping(value = "/rest/annotate", consumes = APPLICATION_FORM_URLENCODED_VALUE, produces = APPLICATION_JSON_VALUE)
    DBPediaAnnotation annotate(@RequestBody Map<String, ?> form);
}
