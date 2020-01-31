package ai.semplify.indexer.services;

import ai.semplify.indexer.models.SearchHits;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    SearchHits search(String q, Pageable page);

    /*
    Flux<Doc> findAll(Pageable page);

    Mono<SearchResult> search(String q, Pageable page);

     */
}
