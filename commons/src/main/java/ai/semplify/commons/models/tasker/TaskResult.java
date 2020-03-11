package ai.semplify.commons.models.tasker;

import lombok.Data;

@Data
public class TaskResult {
    private Long id;
    private String name;
    private byte[] value;
}
