package ai.semplify.tasker.services.impl;

import ai.semplify.commons.models.tasker.TaskStatus;
import ai.semplify.commons.models.tasker.TaskType;
import ai.semplify.tasker.entities.postgresql.Task;
import ai.semplify.tasker.entities.postgresql.TaskParameter;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.Params;
import ai.semplify.tasker.services.TaskHandler;
import ai.semplify.tasker.utils.Utils;
import lombok.var;
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

                    var fileIds = Utils.getParams(t, Params.FILE_ID);

                    var subTasks = t.getSubTasks();
                    fileIds.forEach(f -> {
                        var subTask = spawnSubtasks(t, Long.valueOf(f.getValue()));
                        subTask.setParentTask(t);
                        subTasks.add(subTask);
                    });

                    t.setNumberOfSubTasks(fileIds.size());
                    t.setNumberOfFinishedSubTasks(0);
                    t.setTaskStatus(TaskStatus.SUBTASKS_SPAWNED);
                });
    }

    private Task spawnSubtasks(Task parentTask, Long fileId) {
        var task = new Task();
        task.setParentTask(parentTask);
        task.setScheduled(new Date());
        task.setType(TaskType.FILE_ANNOTATION);

        var param = new TaskParameter();
        param.setTask(task);
        param.setName(Params.FILE_ID);
        param.setValue(fileId.toString());

        task.setParameters(Collections.singletonList(param));
        return task;
    }

}
