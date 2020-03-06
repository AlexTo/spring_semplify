package ai.semplify.tasker.services.impl;

import ai.semplify.commons.webclients.EntityHubWebClient;
import ai.semplify.tasker.models.TaskStatus;
import ai.semplify.tasker.repositories.TaskRepository;
import ai.semplify.tasker.services.TaskHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "FileAnnotationTaskHandler")
@Transactional
public class FileAnnotationTaskHandler implements TaskHandler {

    private TaskRepository taskRepository;
    private Logger logger = LoggerFactory.getLogger(FileAnnotationTaskHandler.class);
    private ObjectMapper objectMapper;
    private EntityHubWebClient entityHubWebClient;

    public FileAnnotationTaskHandler(TaskRepository taskRepository,
                                     ObjectMapper objectMapper,
                                     EntityHubWebClient entityHubWebClient) {
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
        this.entityHubWebClient = entityHubWebClient;
    }

    @Override
    public void process(Long taskId) {
        taskRepository.findByIdAndTaskStatusIsNull(taskId)
                .ifPresent(t -> {
                    try {
                        logger.info("Processing task " + taskId + " | " + t.getType());
                        var fileId = Long.valueOf(t.getParameters().get(0).getValue());

                        var annotation = entityHubWebClient.nerAnnotateServerFile(fileId);

                        var result = objectMapper.writeValueAsString(annotation);
                        t.setResults(result);
                    } catch (Exception e) {
                        if (e.getMessage() != null) {
                            t.setError(e.getMessage());
                        } else {
                            t.setError("ERROR");
                        }
                    }
                    t.setTaskStatus(TaskStatus.FINISHED.getValue());
                    taskRepository.save(t);

                });
    }
}
