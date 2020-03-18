package ai.semplify.commons.models.tasker;

import lombok.Data;

@Data
public class TaskQuery {
    private Long parentTaskId;
    private Integer page;
    private Integer size;
}
