package ai.semplify.tasker.mappers;

import ai.semplify.tasker.entities.postgresql.Task;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TaskParameterMapper.class})
public interface TaskMapper {

    Task toEntity(ai.semplify.tasker.models.Task model);

    ai.semplify.tasker.entities.redis.Task toRedis(ai.semplify.tasker.models.Task task);

    ai.semplify.tasker.models.Task toModel(Task entity);

    List<ai.semplify.tasker.models.Task> toModels(List<Task> entities);
}
