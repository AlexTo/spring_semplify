package ai.semplify.graphql.services.impl;

import ai.semplify.graphql.models.Doc;
import ai.semplify.graphql.services.IndexService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class IndexServiceImpl implements IndexService {

    private WebClient indexerWebClient;

    public IndexServiceImpl(@Qualifier("indexerWebClient") WebClient indexerWebClient) {
        this.indexerWebClient = indexerWebClient;
    }

    @Override
    public Flux<Doc> findAll() {
        return indexerWebClient.get().uri("/search").retrieve().bodyToFlux(Doc.class);
    }
}
