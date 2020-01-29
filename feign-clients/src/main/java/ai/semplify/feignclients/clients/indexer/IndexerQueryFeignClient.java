package ai.semplify.feignclients.clients.indexer;

import ai.semplify.feignclients.clients.indexer.models.Doc;
import ai.semplify.feignclients.config.DefaultFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "indexer", configuration = DefaultFeignClientConfiguration.class)
public interface IndexerQueryFeignClient {

    @GetMapping("/search")
    List<Doc> findAll();
}
