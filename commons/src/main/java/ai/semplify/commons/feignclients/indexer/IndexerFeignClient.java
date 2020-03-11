package ai.semplify.commons.feignclients.indexer;

import ai.semplify.commons.feignclients.DefaultFeignClientConfiguration;
import ai.semplify.commons.models.entityhub.CrawlRequest;
import ai.semplify.commons.models.entityhub.CrawlResponse;
import ai.semplify.commons.models.indexer.Query;
import ai.semplify.commons.models.indexer.SearchHits;
import ai.semplify.commons.models.indexer.SuggestionRequest;
import ai.semplify.commons.models.indexer.Suggestions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "indexer", configuration = DefaultFeignClientConfiguration.class)
public interface IndexerFeignClient {

    @PostMapping("/search")
    SearchHits search(@Valid @RequestBody Query query);

    @PostMapping("/suggest")
    Suggestions suggest(@Valid @RequestBody SuggestionRequest request);

    @PostMapping("/crawler")
    CrawlResponse crawl(@Valid @RequestBody CrawlRequest request);
}
