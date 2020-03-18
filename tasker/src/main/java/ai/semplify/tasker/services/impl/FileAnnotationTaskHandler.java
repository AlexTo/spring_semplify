package ai.semplify.tasker.services.impl;

import ai.semplify.commons.feignclients.entityhub.EntityHubFeignClient;
import ai.semplify.commons.feignclients.fileserver.FileServerFeignClient;
import ai.semplify.commons.models.fileserver.File;
import ai.semplify.commons.models.fileserver.FileAnnotation;
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
@Service(value = "FileAnnotationTaskHandler")
public class FileAnnotationTaskHandler implements TaskHandler {

    private Logger logger = LoggerFactory.getLogger(FileAnnotationTaskHandler.class);
    private TaskRepository taskRepository;
    private EntityHubFeignClient entityHubFeignClient;
    private FileServerFeignClient fileServerFeignClient;
    private TaskService taskService;

    public FileAnnotationTaskHandler(TaskRepository taskRepository,
                                     EntityHubFeignClient entityHubFeignClient,
                                     FileServerFeignClient fileServerFeignClient,
                                     TaskService taskService) {
        this.taskRepository = taskRepository;
        this.entityHubFeignClient = entityHubFeignClient;
        this.fileServerFeignClient = fileServerFeignClient;
        this.taskService = taskService;
    }

    @Override
    public void process(Long taskId) {
        taskRepository.findByIdAndTaskStatusIsNull(taskId)
                .ifPresent(t -> {
                    try {
                        var fileId = Utils.getOneParam(t, Params.FILE_ID);
                        logger.info("Task " + taskId + " | " + t.getType() + "(FileId = " + fileId + ") started");
                        var annotation = entityHubFeignClient.annotateServerFile(Long.valueOf(fileId));
                        var file = new File();
                        file.setId(Long.valueOf(fileId));
                        var fileAnnotation = new FileAnnotation();
                        fileAnnotation.setFile(file);
                        fileAnnotation.setAnnotationResources(annotation.getResources());
                        fileServerFeignClient.createAnnotations(fileAnnotation);
                        logger.info("Task " + taskId + " | " + t.getType() + "(FileId = " + fileId + ") finished");
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
