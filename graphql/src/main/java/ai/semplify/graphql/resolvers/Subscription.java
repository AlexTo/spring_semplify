package ai.semplify.graphql.resolvers;

import ai.semplify.graphql.models.Doc;
import ai.semplify.graphql.services.IndexService;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class Subscription implements GraphQLSubscriptionResolver {

    private Logger logger = LoggerFactory.getLogger(Subscription.class);

    private IndexService indexService;

    public Subscription(IndexService indexService) {
        this.indexService = indexService;
    }

    public Publisher<Integer> search(DataFetchingEnvironment env) {
        return Flux.range(0, 100).delayElements(Duration.ofSeconds(1));

    }
}
