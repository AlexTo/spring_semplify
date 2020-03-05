package ai.semplify.tasker.entities.postgresql;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity(name = "semplify_tasks_parameters")
public class TaskParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    @NotBlank
    private String name;

    @NotBlank
    private String value;
}
