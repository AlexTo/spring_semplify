package ai.semplify.tasker.controllers;

import ai.semplify.commons.models.tasker.Task;
import ai.semplify.commons.models.tasker.TaskPage;
import ai.semplify.tasker.services.TaskService;
import lombok.var;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(task));
    }

    @GetMapping("/")
    public ResponseEntity<TaskPage> findAll(@RequestParam(required = false) Long parentTaskId,
                                            @RequestParam(required = false, defaultValue = "0") Integer page,
                                            @RequestParam(required = false, defaultValue = "20") Integer size) {
        var pageable = PageRequest.of(page, size);
        var result = parentTaskId != null
                ? taskService.findAll(parentTaskId, pageable)
                : taskService.findTopLevelTasks(pageable);
        return ResponseEntity.ok(result);
    }

}

