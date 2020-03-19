package ai.semplify.graphql.models;

import lombok.Data;

@Data
public class TaskQueryInput {
    private Long parentTaskId;
    private Integer page;
    private Integer size;
}
