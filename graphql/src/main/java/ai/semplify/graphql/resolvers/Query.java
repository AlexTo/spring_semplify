package ai.semplify.graphql.resolvers;

import ai.semplify.graphql.models.Doc;
import ai.semplify.graphql.services.IndexService;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class Query implements GraphQLQueryResolver {
    private IndexService indexService;

    public Query(IndexService indexService) {
        this.indexService = indexService;
    }

    public CompletableFuture<List<Doc>> search() {
        return indexService
                .findAll()
                .collectList()
                .toFuture();
    }
}
