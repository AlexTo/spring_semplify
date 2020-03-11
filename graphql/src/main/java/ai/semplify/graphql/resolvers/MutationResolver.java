package ai.semplify.graphql.resolvers;

import ai.semplify.commons.feignclients.tasker.TaskerFeignClient;
import ai.semplify.commons.models.tasker.Task;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

@Component
public class MutationResolver implements GraphQLMutationResolver {
    private TaskerFeignClient taskerFeignClient;

    public MutationResolver(TaskerFeignClient taskerFeignClient) {
        this.taskerFeignClient = taskerFeignClient;
    }

    public Task createTask(Task task) {
        return taskerFeignClient.create(task);
    }
}
