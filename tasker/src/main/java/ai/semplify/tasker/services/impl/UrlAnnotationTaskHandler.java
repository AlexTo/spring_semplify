package ai.semplify.tasker.services.impl;

import ai.semplify.commons.feignclients.entityhub.EntityHubFeignClient;
import ai.semplify.commons.models.entityhub.UrlAnnotationRequest;
import ai.semplify.commons.models.tasker.TaskStatus;
import ai.semplify.tasker.entities.postgresql.TaskResult;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.Params;
import ai.semplify.tasker.services.TaskHandler;
import ai.semplify.tasker.services.TaskService;
import ai.semplify.tasker.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(value = "UrlAnnotationTaskHandler")
public class UrlAnnotationTaskHandler implements TaskHandler {

    private Logger logger = LoggerFactory.getLogger(UrlAnnotationTaskHandler.class);
    private TaskRepository taskRepository;
    private TaskService taskService;
    private ObjectMapper objectMapper;
    private EntityHubFeignClient entityHubFeignClient;

    public UrlAnnotationTaskHandler(TaskRepository taskRepository,
                                    TaskService taskService, ObjectMapper objectMapper,
                                    EntityHubFeignClient entityHubFeignClient) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.objectMapper = objectMapper;
        this.entityHubFeignClient = entityHubFeignClient;
    }

    @Override
    public void process(Long taskId) {
        taskRepository.findByIdAndTaskStatusIsNull(taskId)
                .ifPresent(t -> {
                    try {

                        var url = Utils.getOneParam(t, Params.URL);

                        logger.info("Task " + taskId + " | " + t.getType() + "(Url = " + url + ") started");

                        var request = new UrlAnnotationRequest();
                        request.setUrl(url);
                        var annotation = entityHubFeignClient.annotateUrl(request);

                        var results = t.getResults();
                        var result = new TaskResult();
                        result.setName("annotation");
                        result.setValue(objectMapper.writeValueAsString(annotation).getBytes());
                        result.setTask(t);
                        results.add(result);
                        logger.info("Task " + taskId + " | " + t.getType() + "(Url = " + url + ") finished");

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
}
