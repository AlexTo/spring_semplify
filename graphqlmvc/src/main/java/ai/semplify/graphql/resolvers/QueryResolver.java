package ai.semplify.graphql.resolvers;

import ai.semplify.feignclients.clients.indexer.IndexerFeignClient;
import ai.semplify.feignclients.clients.indexer.models.Query;
import ai.semplify.feignclients.clients.indexer.models.SearchHits;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class QueryResolver implements GraphQLQueryResolver {
    private IndexerFeignClient indexerFeignClient;

    public QueryResolver(IndexerFeignClient indexerFeignClient) {
        this.indexerFeignClient = indexerFeignClient;
    }

    public SearchHits search(Query query) {
        return indexerFeignClient.search(query);
    }
}
