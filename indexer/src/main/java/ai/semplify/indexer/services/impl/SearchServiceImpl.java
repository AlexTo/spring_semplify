package ai.semplify.indexer.services.impl;

import ai.semplify.indexer.mappers.SearchHitsMapper;
import ai.semplify.indexer.models.SearchHits;
import ai.semplify.indexer.services.SearchService;
import lombok.var;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SearchServiceImpl implements SearchService {

    private IndexCoordinates indexCoordinates;
    private ElasticsearchOperations elasticsearchOperations;
    private SearchHitsMapper searchHitsMapper;


    public SearchServiceImpl(IndexCoordinates indexCoordinates,
                             ElasticsearchOperations elasticsearchOperations,
                             SearchHitsMapper searchHitsMapper) {
        this.indexCoordinates = indexCoordinates;
        this.elasticsearchOperations = elasticsearchOperations;
        this.searchHitsMapper = searchHitsMapper;
    }

    @Override
    public SearchHits search(String q, Pageable page) {

        var criteria = (q == null || Objects.equals(q, ""))
                ? new Criteria()
                : new Criteria("content").contains(q);

        var query = new CriteriaQuery(criteria, page);
        var searchHits = elasticsearchOperations.search(query, ai.semplify.indexer.entities.Doc.class, indexCoordinates);
        return searchHitsMapper.toModel(searchHits);
    }
}
