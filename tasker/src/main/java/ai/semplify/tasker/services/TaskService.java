package ai.semplify.tasker.services;

import ai.semplify.commons.models.tasker.Task;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task save(Task task);

    Optional<Task> findById(Long id);

    Optional<Task> findPendingTaskById(Long id);

    List<Task> findPendingTasks(Pageable pageable);
}
