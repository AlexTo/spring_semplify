package ai.semplify.graphql.resolvers;

import ai.semplify.commons.feignclients.fileserver.FileServerFeignClient;
import ai.semplify.commons.feignclients.tasker.TaskerFeignClient;
import ai.semplify.commons.models.fileserver.FileAnnotation;
import ai.semplify.commons.models.tasker.Task;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

@Component
public class MutationResolver implements GraphQLMutationResolver {
    private TaskerFeignClient taskerFeignClient;
    private FileServerFeignClient fileServerFeignClient;

    public MutationResolver(TaskerFeignClient taskerFeignClient,
                            FileServerFeignClient fileServerFeignClient) {
        this.taskerFeignClient = taskerFeignClient;
        this.fileServerFeignClient = fileServerFeignClient;
    }

    public Task createTask(Task task) {
        return taskerFeignClient.create(task);
    }

    public FileAnnotation updateFileAnnotation(FileAnnotation fileAnnotation) {
        return fileServerFeignClient.update(fileAnnotation.getId(), fileAnnotation);
    }
}
