package ai.semplify.tasker.entities.redis;

import lombok.Data;

import java.io.Serializable;

@Data
public class Task implements Serializable {
    private Long id;
    private String type;
}
