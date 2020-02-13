package ai.semplify.entityhub.controllers;

import ai.semplify.entityhub.models.TypeCheckRequest;
import ai.semplify.entityhub.models.TypeCheckResponse;
import ai.semplify.entityhub.services.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/entity")
public class EntityController {

    private EntityService entityService;

    public EntityController(EntityService entityService) {
        this.entityService = entityService;
    }

    @PostMapping("funcs/isSubClassOfTransitive")
    public ResponseEntity<TypeCheckResponse> isSubClassOfTransitive(@Valid @RequestBody TypeCheckRequest request) {
        return ResponseEntity.ok(entityService.isSubClassOfTransitive(request));
    }
}