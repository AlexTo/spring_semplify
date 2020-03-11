package ai.semplify.tasker.services.impl;

import ai.semplify.commons.models.tasker.TaskStatus;
import ai.semplify.commons.models.tasker.TaskType;
import ai.semplify.tasker.entities.postgresql.Task;
import ai.semplify.tasker.entities.postgresql.TaskParameter;
import ai.semplify.tasker.exceptions.MissingParameterException;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.Params;
import ai.semplify.tasker.services.TaskHandler;
import ai.semplify.tasker.utils.Utils;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Transactional
@Service(value = "UrlsIntegrationTaskHandler")
public class UrlsIntegrationTaskHandler implements TaskHandler {

    private TaskRepository taskRepository;

    public UrlsIntegrationTaskHandler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void process(Long taskId) {
        taskRepository.findByIdAndTaskStatusIsNull(taskId)
                .ifPresent(t -> {
                    try {

                        var seedUrls = Utils.getParams(t, Params.seedUrl);

                        var depth = Utils.getOneParam(t, Params.depth);


                        var subTasks = t.getSubTasks();
                        seedUrls.forEach(f -> {
                            var subTask = spawnSubtasks(t, f.getValue(), depth);
                            subTask.setParentTask(t);
                            subTasks.add(subTask);
                        });

                        t.setNumberOfSubTasks(seedUrls.size());
                        t.setNumberOfFinishedSubTasks(0);
                        t.setTaskStatus(TaskStatus.SUBTASKS_SPAWNED.getValue());
                    } catch (Exception e) {
                        if (e.getMessage() != null) {
                            t.setError(e.getMessage());
                        } else {
                            t.setError("ERROR");
                        }
                        t.setTaskStatus(TaskStatus.FINISHED.getValue());
                    }
                });
    }

    private Task spawnSubtasks(Task parentTask, String seedUrl, String depth) {
        var task = new Task();
        task.setParentTask(parentTask);
        task.setScheduled(new Date());
        task.setType(TaskType.UrlCrawler.getValue());

        var seedUrlParam = new TaskParameter();
        seedUrlParam.setTask(task);
        seedUrlParam.setName(Params.seedUrl);
        seedUrlParam.setValue(seedUrl);

        var depthParam = new TaskParameter();
        depthParam.setTask(task);
        depthParam.setName(Params.depth);
        depthParam.setValue(depth);

        var params = new ArrayList<TaskParameter>();
        params.add(seedUrlParam);
        params.add(depthParam);
        task.setParameters(params);

        return task;
    }
}
