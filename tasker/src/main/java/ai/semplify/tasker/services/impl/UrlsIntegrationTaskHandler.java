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

import java.util.ArrayList;
import java.util.Date;

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

                        var seedUrls = Utils.getParams(t, Params.SEED_URL);

                        var depth = Utils.getOneParam(t, Params.DEPTH);


                        var subTasks = t.getSubTasks();
                        seedUrls.forEach(f -> {
                            var subTask = spawnSubtasks(t, f.getValue(), depth);
                            subTask.setParentTask(t);
                            subTasks.add(subTask);
                        });

                        t.setNumberOfSubtasks(seedUrls.size());
                        t.setNumberOfFinishedSubtasks(0);
                        t.setTaskStatus(TaskStatus.SUBTASKS_SPAWNED);
                    } catch (Exception e) {
                        if (e.getMessage() != null) {
                            t.setError(e.getMessage());
                        } else {
                            t.setError("ERROR");
                        }
                        t.setTaskStatus(TaskStatus.FINISHED);
                    }
                });
    }

    private Task spawnSubtasks(Task parentTask, String seedUrl, String depth) {
        var task = new Task();
        task.setParentTask(parentTask);
        task.setScheduled(new Date());
        task.setType(TaskType.URL_CRAWLER);

        var seedUrlParam = new TaskParameter();
        seedUrlParam.setTask(task);
        seedUrlParam.setName(Params.SEED_URL);
        seedUrlParam.setValue(seedUrl);

        var depthParam = new TaskParameter();
        depthParam.setTask(task);
        depthParam.setName(Params.DEPTH);
        depthParam.setValue(depth);

        var params = new ArrayList<TaskParameter>();
        params.add(seedUrlParam);
        params.add(depthParam);
        task.setParameters(params);

        return task;
    }
}
