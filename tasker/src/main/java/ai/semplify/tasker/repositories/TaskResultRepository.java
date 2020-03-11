package ai.semplify.tasker.repositories;

import ai.semplify.tasker.entities.postgresql.TaskResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskResultRepository extends JpaRepository<TaskResult, Long> {
}
