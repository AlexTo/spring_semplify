package ai.semplify.tasker.services.impl;

import ai.semplify.commons.feignclients.indexer.IndexerFeignClient;
import ai.semplify.commons.models.entityhub.CrawlRequest;
import ai.semplify.commons.models.tasker.TaskStatus;
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
                        var seedUrl = Utils.getOneParam(t, Params.seedUrl);
                        var depth = Utils.getOneParam(t, Params.depth);

                        logger.info(String.format("Task %s | %s (seedUrl = %s, depth = %s) started",
                                taskId, t.getType(), seedUrl, depth));

                        var crawlRequest = new CrawlRequest();
                        crawlRequest.setUrl(seedUrl);
                        crawlRequest.setDepth(Integer.valueOf(depth));
                        var crawlResponse = indexerFeignClient.crawl(crawlRequest);


                        logger.info(String.format("Task %s | %s (seedUrl = %s, depth = %s) finished",
                                taskId, t.getType(), seedUrl, depth));
                    } catch (Exception e) {
                        if (e.getMessage() != null) {
                            t.setError(e.getMessage());
                        } else {
                            t.setError("ERROR");
                        }
                    }
                    t.setTaskStatus(TaskStatus.FINISHED.getValue());
                    taskService.updateParentTask(t);
                });
    }
}
