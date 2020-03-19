package ai.semplify.tasker.entities.postgresql;

import ai.semplify.tasker.listeners.RootAware;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "semplify_tasks",
        indexes = {
                @Index(name = "idx_semplify_tasks_type", columnList = "type"),
                @Index(name = "idx_semplify_tasks_task_status", columnList = "taskStatus"),
        })
@EntityListeners(AuditingEntityListener.class)
public class Task implements RootAware<Task> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @NotNull
    private String type;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "task",
            fetch = FetchType.LAZY
    )
    private List<TaskParameter> parameters;

    @ManyToOne
    private Task parentTask;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "parentTask",
            fetch = FetchType.LAZY
    )
    private List<Task> subTasks;

    private String taskStatus;

    private Integer numberOfSubtasks;

    private Integer numberOfFinishedSubtasks;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "task",
            fetch = FetchType.LAZY
    )
    private List<TaskResult> results;

    @Column(length = 65535)
    private String error;

    @NotNull
    private Date scheduled;

    @NotNull
    @CreatedDate
    private Date createdDate;

    @NotNull
    @LastModifiedDate
    private Date lastModifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @Version
    private int version;

    @Override
    public Task root() {
        return parentTask;
    }
}
