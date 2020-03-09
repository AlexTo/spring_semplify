package ai.semplify.tasker.services.impl;

import ai.semplify.commons.models.tasker.TaskStatus;
import ai.semplify.tasker.mappers.TaskMapper;
import ai.semplify.commons.models.tasker.Task;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.TaskService;
import lombok.var;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
    private RedisTemplate<String, ai.semplify.tasker.entities.redis.Task> taskRedisTemplate;
    private ChannelTopic pendingTasksChannel;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskMapper taskMapper,
                           RedisTemplate<String, ai.semplify.tasker.entities.redis.Task> taskRedisTemplate,
                           @Qualifier("pendingTasksTopic") ChannelTopic pendingTasksChannel) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskRedisTemplate = taskRedisTemplate;
        this.pendingTasksChannel = pendingTasksChannel;
    }

    @Override
    public Task save(Task task) {
        var entity = taskMapper.toEntity(task);

        var params = entity.getParameters();
        if (params != null) {
            for (var param : params) {
                param.setTask(entity);
            }
        }

        if (entity.getScheduled() == null) {
            entity.setScheduled(new Date());
        }

        entity = taskRepository.save(entity);
        return taskMapper.toModel(entity);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id)
                .map(t -> taskMapper.toModel(t));
    }

    @Override
    public Optional<Task> findPendingTaskById(Long id) {
        return taskRepository.findByIdAndTaskStatusIsNull(id)
                .map(t -> taskMapper.toModel(t));
    }

    @Override
    public List<Task> findPendingTasks(Pageable pageable) {
        var entities = taskRepository.findAllByTaskStatusIsNull(pageable);
        return taskMapper.toModels(entities);
    }

    @Override
    public void updateParentTask(ai.semplify.tasker.entities.postgresql.Task task) {
        if (task.getParentTask() != null) {
            var parentTask = task.getParentTask();

            if (parentTask.getNumberOfFinishedSubTasks() == null) {
                parentTask.setNumberOfFinishedSubTasks(1);
            } else {
                parentTask.setNumberOfFinishedSubTasks(parentTask.getNumberOfFinishedSubTasks() + 1);
            }

            if (parentTask.getNumberOfSubTasks().equals(parentTask.getNumberOfFinishedSubTasks())) {
                parentTask.setTaskStatus(TaskStatus.FINISHED.getValue());
            }
        }
    }

    public List<Task> findAll() {
        var entities = taskRepository.findAll();
        return taskMapper.toModels(entities);
    }
}
