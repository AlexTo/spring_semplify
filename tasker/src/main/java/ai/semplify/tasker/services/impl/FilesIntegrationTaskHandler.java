package ai.semplify.tasker.services.impl;

import ai.semplify.tasker.entities.postgresql.Task;
import ai.semplify.tasker.entities.postgresql.TaskParameter;
import ai.semplify.commons.models.tasker.TaskStatus;
import ai.semplify.commons.models.tasker.TaskType;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.TaskHandler;
import ai.semplify.tasker.services.TaskService;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Service(value = "FilesIntegrationTaskHandler")
@Transactional
public class FilesIntegrationTaskHandler implements TaskHandler {

    private final String PARAM_FILE_ID = "file_id";
    private TaskRepository taskRepository;
    private Logger logger = LoggerFactory.getLogger(FilesIntegrationTaskHandler.class);
    private TaskService taskService;

    public FilesIntegrationTaskHandler(TaskRepository taskRepository,
                                       TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @Override
    public void process(Long taskId) {
        taskRepository.findByIdAndTaskStatusIsNull(taskId)
                .ifPresent(t -> {
                    logger.info("Processing task " + taskId + " | " + t.getType());
                    var params = t.getParameters();
                    var fileIds = params.stream().filter(p -> p.getName().equals(PARAM_FILE_ID))
                            .collect(Collectors.toList());

                    fileIds.forEach(f -> {
                        spawnSubtasks(t, Long.valueOf(f.getValue()));
                    });

                    t.setNumberOfSubTasks(fileIds.size());
                    t.setNumberOfFinishedSubTasks(0);
                    t.setTaskStatus(TaskStatus.SUBTASKS_SPAWNED.getValue());
                    taskRepository.save(t);
                });
    }

    private void spawnSubtasks(Task parentTask, Long fileId) {
        var task = new Task();
        task.setParentTask(parentTask);
        task.setScheduled(new Date());
        task.setType(TaskType.FileAnnotation.getValue());

        var param = new TaskParameter();
        param.setTask(task);
        param.setName(PARAM_FILE_ID);
        param.setValue(fileId.toString());

        task.setParameters(Collections.singletonList(param));
        taskRepository.save(task);
    }

}
