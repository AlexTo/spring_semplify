package ai.semplify.indexer.services;

import ai.semplify.indexer.models.Query;
import ai.semplify.indexer.models.SearchHits;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    SearchHits search(Query query);
}
