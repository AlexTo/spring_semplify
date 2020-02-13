package ai.semplify.feignclients.clients.poolparty;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@FeignClient(value = "poolparty",
        url = "${poolparty.host}:${poolparty.port}/extractor/api",
        configuration = {PoolPartyFeignClientConfiguration.class})
public interface PoolPartyExtractorFeignClient {

    @PostMapping(value = "/annotate",
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_XML_VALUE)
    String annotate(@RequestBody Map<String, ?> request);
}
