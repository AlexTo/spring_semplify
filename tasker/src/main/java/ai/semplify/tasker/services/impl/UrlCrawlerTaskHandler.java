package ai.semplify.tasker.services.impl;

import ai.semplify.commons.feignclients.indexer.IndexerFeignClient;
import ai.semplify.commons.models.entityhub.CrawlRequest;
import ai.semplify.commons.models.tasker.TaskStatus;
import ai.semplify.commons.models.tasker.TaskType;
import ai.semplify.tasker.entities.postgresql.Task;
import ai.semplify.tasker.entities.postgresql.TaskParameter;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.Params;
import ai.semplify.tasker.services.TaskHandler;
import ai.semplify.tasker.services.TaskService;
import ai.semplify.tasker.utils.Utils;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@Transactional
@Service(value = "UrlCrawlerTaskHandler")
public class UrlCrawlerTaskHandler implements TaskHandler {
    private TaskRepository taskRepository;
    private Logger logger = LoggerFactory.getLogger(UrlCrawlerTaskHandler.class);
    private TaskService taskService;
    private IndexerFeignClient indexerFeignClient;

    public UrlCrawlerTaskHandler(TaskRepository taskRepository,
                                 TaskService taskService,
                                 IndexerFeignClient indexerFeignClient) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.indexerFeignClient = indexerFeignClient;
    }

    @Override
    public void process(Long taskId) {
        taskRepository.findByIdAndTaskStatusIsNull(taskId)
                .ifPresent(t -> {
                    try {
                        var seedUrl = Utils.getOneParam(t, Params.SEED_URL);
                        var depth = Utils.getOneParam(t, Params.DEPTH);

                        logger.info(String.format("Task %s | %s (seedUrl = %s, depth = %s) started",
                                taskId, t.getType(), seedUrl, depth));

                        var crawlRequest = new CrawlRequest();
                        crawlRequest.setUrl(seedUrl);
                        crawlRequest.setDepth(Integer.valueOf(depth));
                        var crawlResponse = indexerFeignClient.crawl(crawlRequest);

                        var subTasks = t.getSubTasks();
                        var urls = crawlResponse.getUrls();
                        urls.forEach(url -> {
                            var subTask = spawnSubtasks(t, url);
                            subTask.setParentTask(t);
                            subTasks.add(subTask);
                        });

                        t.setNumberOfSubTasks(urls.size());
                        t.setNumberOfFinishedSubTasks(0);
                        t.setTaskStatus(TaskStatus.SUBTASKS_SPAWNED);

                        logger.info(String.format("Task %s | %s (seedUrl = %s, depth = %s) finished",
                                taskId, t.getType(), seedUrl, depth));
                    } catch (Exception e) {
                        if (e.getMessage() != null) {
                            t.setError(e.getMessage());
                        } else {
                            t.setError("ERROR");
                        }
                    }
                    t.setTaskStatus(TaskStatus.FINISHED);
                    taskService.updateParentTask(t);
                });
    }

    private Task spawnSubtasks(Task parentTask, String url) {
        var task = new Task();
        task.setParentTask(parentTask);
        task.setScheduled(new Date());
        task.setType(TaskType.URL_ANNOTATION);

        var urlParam = new TaskParameter();
        urlParam.setTask(task);
        urlParam.setName(Params.URL);
        urlParam.setValue(url);

        var params = new ArrayList<TaskParameter>();
        params.add(urlParam);
        task.setParameters(params);

        return task;
    }
}
