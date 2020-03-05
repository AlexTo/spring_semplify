package ai.semplify.tasker.listeners;

import ai.semplify.tasker.components.ApplicationContextHolder;
import ai.semplify.tasker.entities.redis.Task;
import ai.semplify.tasker.services.TaskHandler;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Component;

@Component
public class TaskMessageListener {

    private ApplicationContextHolder context;
    private Logger logger = LoggerFactory.getLogger(TaskMessageListener.class);

    public TaskMessageListener(ApplicationContextHolder context) {
        this.context = context;
    }

    public void handleMessage(Task task) {
        try {
            var handler = context.getBean(task.getType() + "TaskHandler", TaskHandler.class);
            handler.process(task.getId());
        } catch (NoSuchBeanDefinitionException e) {
            logger.error("Handler for task " + task.getType() + " not found");
        }
    }
}
