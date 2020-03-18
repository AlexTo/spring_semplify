package ai.semplify.commons.models.tasker;

import lombok.Data;

import java.util.List;

@Data
public class TaskPage {
    private List<Task> tasks;
    private Long totalElements;
    private Integer totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Boolean isEmpty;
}
