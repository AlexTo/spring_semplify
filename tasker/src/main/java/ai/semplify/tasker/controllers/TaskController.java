package ai.semplify.tasker.controllers;

import ai.semplify.tasker.models.Task;
import ai.semplify.tasker.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.Valid;
import java.security.Principal;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;
    private WebClient webClient;

    public TaskController(TaskService taskService,
                          WebClient webClient) {
        this.taskService = taskService;
        this.webClient = webClient;
    }

    @PostMapping("/")
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(task));
    }

    @GetMapping("/")
    public ResponseEntity<Void> test(Principal principal) {
        webClient.get().uri("http://entity-hub/ner")
                .attributes(clientRegistrationId("semplify-service"))
                .retrieve().bodyToMono(String.class).block();
        return ResponseEntity.ok().build();
    }
}

