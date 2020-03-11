package ai.semplify.tasker.services.impl;

import ai.semplify.commons.models.tasker.TaskStatus;
import ai.semplify.commons.models.tasker.TaskType;
import ai.semplify.tasker.entities.postgresql.Task;
import ai.semplify.tasker.entities.postgresql.TaskParameter;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.Params;
import ai.semplify.tasker.services.TaskHandler;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Transactional
@Service(value = "FilesIntegrationTaskHandler")
public class FilesIntegrationTaskHandler implements TaskHandler {

    private TaskRepository taskRepository;

    public FilesIntegrationTaskHandler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void process(Long taskId) {
        taskRepository.findByIdAndTaskStatusIsNull(taskId)
                .ifPresent(t -> {
                    var params = t.getParameters();
                    var fileIds = params.stream().filter(p -> p.getName().equals(Params.fileId))
                            .collect(Collectors.toList());

                    var subTasks = t.getSubTasks();
                    fileIds.forEach(f -> {
                        var subTask = spawnSubtasks(t, Long.valueOf(f.getValue()));
                        subTask.setParentTask(t);
                        subTasks.add(subTask);
                    });

                    t.setNumberOfSubTasks(fileIds.size());
                    t.setNumberOfFinishedSubTasks(0);
                    t.setTaskStatus(TaskStatus.SUBTASKS_SPAWNED.getValue());
                });
    }

    private Task spawnSubtasks(Task parentTask, Long fileId) {
        var task = new Task();
        task.setParentTask(parentTask);
        task.setScheduled(new Date());
        task.setType(TaskType.FileAnnotation.getValue());

        var param = new TaskParameter();
        param.setTask(task);
        param.setName(Params.fileId);
        param.setValue(fileId.toString());

        task.setParameters(Collections.singletonList(param));
        return task;
    }

}
