package ai.semplify.tasker.repositories;

import ai.semplify.tasker.entities.postgresql.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Task> findById(Long id);

    List<Task> findAllByTaskStatusIsNull(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Task> findByIdAndTaskStatusIsNull(Long id);
}
