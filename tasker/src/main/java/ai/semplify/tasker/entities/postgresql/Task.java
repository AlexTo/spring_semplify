package ai.semplify.tasker.entities.postgresql;

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
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @NotNull
    private String type;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "task"
    )
    private List<TaskParameter> parameters;

    @OneToOne
    private Task parentTask;

    private String taskStatus;

    private Integer numberOfSubTasks;

    private Integer numberOfFinishedSubTasks;

    @Lob
    private String results;

    @Lob
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
}
