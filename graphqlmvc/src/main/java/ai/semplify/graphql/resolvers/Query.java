package ai.semplify.graphql.resolvers;

import ai.semplify.feignclients.clients.indexer.IndexerQueryFeignClient;
import ai.semplify.feignclients.clients.indexer.models.Doc;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {
    private IndexerQueryFeignClient indexerQueryFeignClient;

    public Query(IndexerQueryFeignClient indexerQueryFeignClient) {
        this.indexerQueryFeignClient = indexerQueryFeignClient;
    }

    public List<Doc> search() {
        return indexerQueryFeignClient.findAll();
    }
}
