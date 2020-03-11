package ai.semplify.commons.feignclients.tasker;

import ai.semplify.commons.feignclients.DefaultFeignClientConfiguration;
import ai.semplify.commons.models.tasker.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "tasker", configuration = DefaultFeignClientConfiguration.class)
public interface TaskerFeignClient {

    @PostMapping("/tasks/")
    Task create(@Valid @RequestBody Task task);
}
