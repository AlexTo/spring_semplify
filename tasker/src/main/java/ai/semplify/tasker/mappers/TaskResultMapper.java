package ai.semplify.tasker.mappers;

import ai.semplify.tasker.entities.postgresql.TaskResult;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskResultMapper {
    TaskResult toEntity(ai.semplify.commons.models.tasker.TaskResult model);

    ai.semplify.commons.models.tasker.TaskResult toModel(TaskResult entity);
}
