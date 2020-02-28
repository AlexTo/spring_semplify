package ai.semplify.feignclients.clients.indexer;

import ai.semplify.feignclients.clients.indexer.models.Query;
import ai.semplify.feignclients.clients.indexer.models.SearchHits;
import ai.semplify.feignclients.clients.indexer.models.SuggestionRequest;
import ai.semplify.feignclients.clients.indexer.models.Suggestions;
import ai.semplify.feignclients.config.DefaultFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "indexer", configuration = DefaultFeignClientConfiguration.class)
public interface IndexerFeignClient {

    @PostMapping("/search")
    SearchHits search(@Valid @RequestBody Query query);

    @PostMapping("/suggest")
    Suggestions suggest(@Valid @RequestBody SuggestionRequest request);
}
