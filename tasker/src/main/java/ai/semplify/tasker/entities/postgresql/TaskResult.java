package ai.semplify.tasker.entities.postgresql;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity(name = "semplify_tasks_results")
public class TaskResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Task task;

    @NotNull
    private String name;

    private byte[] value;
}
