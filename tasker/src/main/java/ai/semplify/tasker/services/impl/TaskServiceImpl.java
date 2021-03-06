package ai.semplify.tasker.services.impl;

import ai.semplify.commons.models.tasker.Task;
import ai.semplify.commons.models.tasker.TaskPage;
import ai.semplify.commons.models.tasker.TaskStatus;
import ai.semplify.tasker.mappers.TaskMapper;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.TaskService;
import lombok.var;
import org.springframework.data.domain.Pageable;
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
    public TaskPage findPendingTasks(Pageable pageable) {
        var entity = taskRepository.findAllByTaskStatusIsNull(pageable);
        return taskMapper.toModel(entity);
    }

    @Override
    public TaskPage findTopLevelTasks(Pageable pageable) {
        var entity = taskRepository.findAllByParentTaskIsNull(pageable);
        return taskMapper.toModel(entity);
    }

    @Override
    public TaskPage findAll(Long parentTaskId, Pageable pageable) {
        var entity = taskRepository.findAllByParentTask_Id(parentTaskId, pageable);
        return taskMapper.toModel(entity);
    }

    @Override
    public void updateParentTask(ai.semplify.tasker.entities.postgresql.Task task) {

        if (task.getParentTask() == null)
            return;

        taskRepository.findById(task.getParentTask().getId()).ifPresent(
                parentTask -> {
                    parentTask.setNumberOfFinishedSubtasks(parentTask.getNumberOfFinishedSubtasks() + 1);
                    if (parentTask.getNumberOfSubtasks().equals(parentTask.getNumberOfFinishedSubtasks())) {
                        parentTask.setTaskStatus(TaskStatus.FINISHED);
                        updateParentTask(parentTask);
                    }
                }
        );

    }

    public List<Task> findAll() {
        var entities = taskRepository.findAll();
        return taskMapper.toModels(entities);
    }
}
