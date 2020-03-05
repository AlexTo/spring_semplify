package ai.semplify.tasker.services.impl;

import ai.semplify.tasker.mappers.TaskMapper;
import ai.semplify.tasker.models.Task;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.TaskService;
import lombok.var;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
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
        return taskMapper.toModel(taskRepository.save(entity));
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

    public List<Task> findAll() {
        var entities = taskRepository.findAll();
        return taskMapper.toModels(entities);
    }
}
