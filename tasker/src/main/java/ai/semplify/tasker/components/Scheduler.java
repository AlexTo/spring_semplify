package ai.semplify.tasker.components;

import ai.semplify.tasker.services.TaskHandler;
import ai.semplify.tasker.services.TaskService;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

@Configuration
public class Scheduler {

    Random rand = new Random();
    private Logger logger = LoggerFactory.getLogger(Scheduler.class);

    private TaskService taskService;
    private ApplicationContextHolder context;

    public Scheduler(TaskService taskService, ApplicationContextHolder context) {
        this.taskService = taskService;
        this.context = context;
    }


    @Scheduled(fixedRate = 5000)
    public void broadcastPendingTasks() {
        var pageable = PageRequest.of(rand.nextInt(2), 1);
        var pendingTasks = taskService.findPendingTasks(pageable);
        for (var task : pendingTasks) {
            try {
                var handler = context.getBean(task.getType() + "TaskHandler", TaskHandler.class);
                handler.process(task.getId());
            } catch (NoSuchBeanDefinitionException e) {
                logger.error("Handler for task " + task.getType() + " not found");
            } catch (ObjectOptimisticLockingFailureException e) {
                logger.warn("ObjectOptimisticLockingFailureException occurred for task " + task.getId() + ", safely ignored");
            }
        }
    }

}
