package ai.semplify.tasker.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskParameter {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String value;
}
