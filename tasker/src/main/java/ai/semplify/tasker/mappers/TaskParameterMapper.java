package ai.semplify.tasker.mappers;

import ai.semplify.commons.models.tasker.TaskParameter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskParameterMapper {

    TaskParameter toModel(ai.semplify.tasker.entities.postgresql.TaskParameter entity);

    @Mappings({
            @org.mapstruct.Mapping(target = "task", ignore = true)
    })
    ai.semplify.tasker.entities.postgresql.TaskParameter toEntity(TaskParameter model);

    List<TaskParameter> toModels(List<ai.semplify.tasker.entities.postgresql.TaskParameter> entities);

    List<ai.semplify.tasker.entities.postgresql.TaskParameter> toEntities(List<TaskParameter> models);
}
