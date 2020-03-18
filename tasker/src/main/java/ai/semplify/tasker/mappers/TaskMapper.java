package ai.semplify.tasker.mappers;

import ai.semplify.commons.models.tasker.TaskPage;
import ai.semplify.tasker.entities.postgresql.Task;
import lombok.var;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TaskParameterMapper.class, TaskResultMapper.class})
public interface TaskMapper {

    @Mappings({
            @org.mapstruct.Mapping(target = "version", ignore = true)
    })
    Task toEntity(ai.semplify.commons.models.tasker.Task model);

    default TaskPage toModel(Page<Task> entity) {
        var taskPage = new TaskPage();
        taskPage.setTasks(entity.get().map(this::toModel).collect(Collectors.toList()));
        taskPage.setTotalElements(entity.getTotalElements());
        taskPage.setTotalPages(entity.getTotalPages());
        taskPage.setHasNext(entity.hasNext());
        taskPage.setHasPrevious(entity.hasPrevious());
        taskPage.setIsEmpty(entity.isEmpty());
        return taskPage;
    }

    ai.semplify.tasker.entities.redis.Task toRedis(ai.semplify.commons.models.tasker.Task task);

    ai.semplify.tasker.entities.redis.Task toRedis(Task entity);

    ai.semplify.commons.models.tasker.Task toModel(Task entity);

    List<ai.semplify.commons.models.tasker.Task> toModels(List<Task> entities);
}
