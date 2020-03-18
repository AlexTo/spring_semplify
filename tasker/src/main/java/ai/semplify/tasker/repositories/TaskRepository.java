package ai.semplify.tasker.repositories;

import ai.semplify.tasker.entities.postgresql.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {


    Optional<Task> findById(Long id);

    Optional<Task> findByIdAndTaskStatusIsNull(Long id);

    Page<Task> findAllByTaskStatusIsNull(Pageable pageable);

    Page<Task> findAllByParentTaskIsNull(Pageable pageable);

    Page<Task> findAllByParentTask_Id(Long parentTaskId, Pageable pageable);

}
