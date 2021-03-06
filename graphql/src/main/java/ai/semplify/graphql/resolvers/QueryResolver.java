package ai.semplify.graphql.resolvers;

import ai.semplify.commons.feignclients.entityhub.EntityHubFeignClient;
import ai.semplify.commons.feignclients.fileserver.FileServerFeignClient;
import ai.semplify.commons.feignclients.indexer.IndexerFeignClient;
import ai.semplify.commons.feignclients.tasker.TaskerFeignClient;
import ai.semplify.commons.models.entityhub.*;
import ai.semplify.commons.models.fileserver.FileAnnotationPage;
import ai.semplify.commons.models.indexer.Query;
import ai.semplify.commons.models.indexer.SearchHits;
import ai.semplify.commons.models.indexer.SuggestionRequest;
import ai.semplify.commons.models.indexer.Suggestions;
import ai.semplify.commons.models.tasker.TaskPage;
import ai.semplify.graphql.models.FileAnnotationQueryInput;
import ai.semplify.graphql.models.TaskQueryInput;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.var;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryResolver implements GraphQLQueryResolver {
    private IndexerFeignClient indexerFeignClient;
    private EntityHubFeignClient entityHubFeignClient;
    private TaskerFeignClient taskerFeignClient;
    private FileServerFeignClient fileServerFeignClient;

    public QueryResolver(IndexerFeignClient indexerFeignClient,
                         EntityHubFeignClient entityHubFeignClient,
                         TaskerFeignClient taskerFeignClient,
                         FileServerFeignClient fileServerFeignClient) {
        this.indexerFeignClient = indexerFeignClient;
        this.entityHubFeignClient = entityHubFeignClient;
        this.taskerFeignClient = taskerFeignClient;
        this.fileServerFeignClient = fileServerFeignClient;
    }

    public SearchHits search(Query query) {
        return indexerFeignClient.search(query);
    }

    public ThumbnailResponse thumbnail(String uri) {
        var request = new ThumbnailRequest();
        request.setUri(uri);
        return entityHubFeignClient.getThumbnail(request);
    }

    public DepictionResponse depiction(String uri) {
        var request = new DepictionRequest();
        request.setUri(uri);
        return entityHubFeignClient.getDepiction(request);
    }

    public EntitySummaryResponse summary(String uri) {
        var request = new EntitySummaryRequest();
        request.setUri(uri);
        return entityHubFeignClient.getSummary(request);
    }

    public TaskPage tasks(TaskQueryInput taskQueryInput) {
        return taskerFeignClient.findAll(taskQueryInput.getParentTaskId(), taskQueryInput.getPage(), taskQueryInput.getSize());
    }

    public Suggestions suggest(SuggestionRequest request) {
        return indexerFeignClient.suggest(request);
    }

    public FileAnnotationPage fileAnnotations(FileAnnotationQueryInput fileAnnotationQueryInput) {
        return fileServerFeignClient.findAll(fileAnnotationQueryInput.getPage(), fileAnnotationQueryInput.getSize());
    }

    public List<AnnotationResource> fileAnnotationResources(Long fileAnnotationId) {
        return fileServerFeignClient.findAll(fileAnnotationId);
    }
}
