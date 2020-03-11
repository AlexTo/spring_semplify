package ai.semplify.tasker.mappers;

import ai.semplify.tasker.entities.postgresql.Task;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TaskParameterMapper.class, TaskResultMapper.class})
public interface TaskMapper {

    @Mappings({
            @org.mapstruct.Mapping(target = "version", ignore = true)
    })
    Task toEntity(ai.semplify.commons.models.tasker.Task model);

    ai.semplify.tasker.entities.redis.Task toRedis(ai.semplify.commons.models.tasker.Task task);

    ai.semplify.tasker.entities.redis.Task toRedis(Task entity);

    ai.semplify.commons.models.tasker.Task toModel(Task entity);

    List<ai.semplify.commons.models.tasker.Task> toModels(List<Task> entities);
}
