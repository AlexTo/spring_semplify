package ai.semplify.graphql.services;

import ai.semplify.graphql.models.Doc;
import reactor.core.publisher.Flux;

public interface IndexService {
    Flux<Doc> findAll();

}
