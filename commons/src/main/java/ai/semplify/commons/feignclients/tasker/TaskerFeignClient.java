package ai.semplify.commons.feignclients.tasker;

import ai.semplify.commons.feignclients.DefaultFeignClientConfiguration;
import ai.semplify.commons.models.tasker.Task;
import ai.semplify.commons.models.tasker.TaskPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(value = "tasker", configuration = DefaultFeignClientConfiguration.class)
public interface TaskerFeignClient {

    @PostMapping("/tasks/")
    Task create(@Valid @RequestBody Task task);

    @GetMapping("/tasks/")
    TaskPage findAll(@RequestParam(required = false) Long parentTaskId,
                     @RequestParam(required = false, defaultValue = "0") Integer page,
                     @RequestParam(required = false, defaultValue = "20") Integer size);
}
