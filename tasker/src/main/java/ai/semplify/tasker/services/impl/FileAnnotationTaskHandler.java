package ai.semplify.tasker.services.impl;

import ai.semplify.commons.feignclients.entityhub.EntityHubFeignClient;
import ai.semplify.commons.models.tasker.TaskStatus;
import ai.semplify.tasker.entities.postgresql.TaskResult;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.repositories.TaskResultRepository;
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
@Service(value = "FileAnnotationTaskHandler")
public class FileAnnotationTaskHandler implements TaskHandler {

    private TaskRepository taskRepository;
    private Logger logger = LoggerFactory.getLogger(FileAnnotationTaskHandler.class);
    private ObjectMapper objectMapper;
    private EntityHubFeignClient entityHubFeignClient;
    private TaskService taskService;

    public FileAnnotationTaskHandler(TaskRepository taskRepository,
                                     ObjectMapper objectMapper,
                                     EntityHubFeignClient entityHubFeignClient,
                                     TaskService taskService) {
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
        this.entityHubFeignClient = entityHubFeignClient;
        this.taskService = taskService;
    }

    @Override
    public void process(Long taskId) {
        taskRepository.findByIdAndTaskStatusIsNull(taskId)
                .ifPresent(t -> {
                    try {
                        var fileId = Utils.getOneParam(t, Params.fileId);
                        logger.info("Task " + taskId + " | " + t.getType() + "(FileId = " + fileId + ") started");

                        var annotation = entityHubFeignClient.annotateServerFile(Long.valueOf(fileId));
                        var results = t.getResults();
                        var result = new TaskResult();
                        result.setName("annotation");
                        result.setValue(objectMapper.writeValueAsString(annotation).getBytes());
                        result.setTask(t);
                        results.add(result);
                        logger.info("Task " + taskId + " | " + t.getType() + "(FileId = " + fileId + ") finished");
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
