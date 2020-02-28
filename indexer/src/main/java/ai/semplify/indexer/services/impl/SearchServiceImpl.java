package ai.semplify.indexer.services.impl;

import ai.semplify.indexer.entities.elasticsearch.Document;
import ai.semplify.indexer.mappers.SearchHitsMapper;
import ai.semplify.indexer.models.Query;
import ai.semplify.indexer.models.SearchHits;
import ai.semplify.indexer.services.SearchService;
import lombok.var;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class SearchServiceImpl implements SearchService {

    private IndexCoordinates documentsIndex;
    private ElasticsearchOperations elasticsearchOperations;
    private SearchHitsMapper searchHitsMapper;


    private Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    public SearchServiceImpl(@Qualifier("documents_index") IndexCoordinates documentsIndex,
                             ElasticsearchOperations elasticsearchOperations,
                             SearchHitsMapper searchHitsMapper) {
        this.documentsIndex = documentsIndex;
        this.elasticsearchOperations = elasticsearchOperations;
        this.searchHitsMapper = searchHitsMapper;
    }

    @Override
    public SearchHits search(Query query) {
        var queryText = query.getQ();
        var page = query.getPage() == null ? 0 : query.getPage();
        var size = query.getSize() == null ? 9 : query.getSize();
        var pageRequest = PageRequest.of(page, size);

        QueryBuilder matchQuery = (queryText == null || Objects.equals(queryText, ""))
                ? matchAllQuery() : matchQuery("content", queryText);

        QueryBuilder selectedAnnotationsQuery = null;

        var selectedAnnotations = query.getSelectedAnnotations();

        if (selectedAnnotations != null && selectedAnnotations.size() > 0) {
            selectedAnnotationsQuery = QueryBuilders.nestedQuery(
                    "annotations",
                    QueryBuilders.termsQuery("annotations.uri", selectedAnnotations), ScoreMode.Avg);
        }

        var aggs = AggregationBuilders
                .nested("annotations", "annotations")
                .subAggregation(
                        AggregationBuilders
                                .nested("classes", "annotations.classes")
                                .subAggregation(
                                        AggregationBuilders
                                                .terms("class_uris")
                                                .field("annotations.classes.uri")
                                                .subAggregation(AggregationBuilders
                                                        .reverseNested("annotation_uris")
                                                        .path("annotations")
                                                        .subAggregation(AggregationBuilders
                                                                .terms("annotations_per_class")
                                                                .field("annotations.uri")))));

        var boolQuery = QueryBuilders.boolQuery();
        boolQuery.must().add(matchQuery);
        if (selectedAnnotationsQuery != null)
            boolQuery.must().add(selectedAnnotationsQuery);

        var request = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withHighlightFields(new HighlightBuilder.Field("content"))
                .addAggregation(aggs)
                .withPageable(pageRequest)
                .build();

        var searchHits = elasticsearchOperations
                .search(request, Document.class, documentsIndex);

        return searchHitsMapper.toModel(searchHits);

    }


}
