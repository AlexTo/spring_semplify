package ai.semplify.indexer.services;

import ai.semplify.commons.models.indexer.Query;
import ai.semplify.commons.models.indexer.SearchHits;

public interface SearchService {

    SearchHits search(Query query);


}
