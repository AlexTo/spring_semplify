package ai.semplify.indexer.services.impl;

import ai.semplify.feignclients.clients.entityhub.EntityHubFeignClient;
import ai.semplify.feignclients.clients.entityhub.models.TypeCheckRequest;
import ai.semplify.indexer.entities.elasticsearch.IndexedDocument;
import ai.semplify.indexer.mappers.SearchHitsMapper;
import ai.semplify.indexer.models.SearchHits;
import ai.semplify.indexer.services.SearchService;
import lombok.var;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class SearchServiceImpl implements SearchService {

    private IndexCoordinates indexCoordinates;
    private ElasticsearchOperations elasticsearchOperations;
    private SearchHitsMapper searchHitsMapper;
    private EntityHubFeignClient entityHubFeignClient;
    private Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    public SearchServiceImpl(IndexCoordinates indexCoordinates,
                             ElasticsearchOperations elasticsearchOperations,
                             SearchHitsMapper searchHitsMapper,
                             EntityHubFeignClient entityHubFeignClient) {
        this.indexCoordinates = indexCoordinates;
        this.elasticsearchOperations = elasticsearchOperations;
        this.searchHitsMapper = searchHitsMapper;
        this.entityHubFeignClient = entityHubFeignClient;
    }

    @Override
    public SearchHits search(String q, Pageable page) {

        QueryBuilder match = (q == null || Objects.equals(q, "")) ? matchAllQuery() : matchQuery("content", q);

        var query = new NativeSearchQueryBuilder()
                .withQuery(match)
                .withHighlightFields(new HighlightBuilder.Field("content"))
                .build();

        var searchHits = elasticsearchOperations.search(query, IndexedDocument.class, indexCoordinates);

        return searchHitsMapper.toModel(searchHits);
    }
}
