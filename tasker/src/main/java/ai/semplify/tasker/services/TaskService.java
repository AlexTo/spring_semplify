package ai.semplify.tasker.services;

import ai.semplify.commons.models.tasker.Task;
import ai.semplify.commons.models.tasker.TaskPage;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {

    Task save(Task task);

    Optional<Task> findById(Long id);

    Optional<Task> findPendingTaskById(Long id);

    TaskPage findPendingTasks(Pageable pageable);

    TaskPage findTopLevelTasks(Pageable pageable);

    TaskPage findAll(Long parentTaskId, Pageable pageable);

    void updateParentTask(ai.semplify.tasker.entities.postgresql.Task task);
}
