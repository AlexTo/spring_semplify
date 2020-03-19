package ai.semplify.commons.models.tasker;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class Task {

    private Long id;

    private String description;

    @NotBlank
    private String type;

    private Integer numberOfSubtasks;

    private Integer numberOfFinishedSubtasks;

    private List<TaskResult> results;

    private String error;

    private Date scheduled;

    private List<TaskParameter> parameters;

    private String taskStatus;

    private Task parentTask;

    private Date createdDate;

    private Date lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;
}
