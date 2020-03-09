package ai.semplify.tasker.components;

import ai.semplify.tasker.entities.redis.Task;
import ai.semplify.tasker.mappers.TaskMapper;
import ai.semplify.tasker.services.TaskService;
import lombok.var;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;

@Configuration
public class Scheduler {

    private int page = 0;
    private int size = 10;

    private RedisTemplate<String, Task> taskRedisTemplate;
    private TaskMapper taskMapper;

    private ChannelTopic pendingTasksChannel;

    private TaskService taskService;

    public Scheduler(RedisTemplate<String, Task> taskRedisTemplate,
                     TaskMapper taskMapper, @Qualifier("pendingTasksTopic") ChannelTopic pendingTasksChannel,
                     TaskService taskService) {
        this.taskRedisTemplate = taskRedisTemplate;
        this.taskMapper = taskMapper;
        this.pendingTasksChannel = pendingTasksChannel;
        this.taskService = taskService;
    }


    @Scheduled(fixedRate = 5000)
    public void broadcastPendingTasks() {
        var pageable = PageRequest.of(page, size);
        var pendingTasks = taskService.findPendingTasks(pageable);
        if (pendingTasks.isEmpty()) {
            page = 0;
        } else {
            for (var task : pendingTasks) {
                var redisTask = taskMapper.toRedis(task);
                taskRedisTemplate.convertAndSend(pendingTasksChannel.getTopic(), redisTask);
            }
            page++;
        }
    }

}
