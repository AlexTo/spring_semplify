package ai.semplify.graphql.resolvers;

import ai.semplify.feignclients.clients.entityhub.EntityHubFeignClient;
import ai.semplify.feignclients.clients.entityhub.models.*;
import ai.semplify.feignclients.clients.indexer.IndexerFeignClient;
import ai.semplify.feignclients.clients.indexer.models.Query;
import ai.semplify.feignclients.clients.indexer.models.SearchHits;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.var;
import org.springframework.stereotype.Component;

@Component
public class QueryResolver implements GraphQLQueryResolver {
    private IndexerFeignClient indexerFeignClient;
    private EntityHubFeignClient entityHubFeignClient;

    public QueryResolver(IndexerFeignClient indexerFeignClient,
                         EntityHubFeignClient entityHubFeignClient) {
        this.indexerFeignClient = indexerFeignClient;
        this.entityHubFeignClient = entityHubFeignClient;
    }

    public SearchHits search(Query query) {
        return indexerFeignClient.search(query);
    }

    public ThumbnailResponse thumbnail(String uri) {
        var request = new ThumbnailRequest();
        request.setUri(uri);
        return entityHubFeignClient.getThumbnail(request);
    }

    public DepictionResponse depiction(String uri) {
        var request = new DepictionRequest();
        request.setUri(uri);
        return entityHubFeignClient.getDepiction(request);
    }

    public EntitySummaryResponse summary(String uri) {
        var request = new EntitySummaryRequest();
        request.setUri(uri);
        return entityHubFeignClient.getSummary(request);
    }
}
