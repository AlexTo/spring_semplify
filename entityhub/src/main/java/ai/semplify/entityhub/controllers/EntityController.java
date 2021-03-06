package ai.semplify.entityhub.controllers;

import ai.semplify.commons.models.entityhub.*;
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

    @PostMapping("funcs/isSubClassOf")
    public ResponseEntity<TypeCheckResponse> isSubClassOf(@Valid @RequestBody TypeCheckRequest request) {
        return ResponseEntity.ok(entityService.isSubClassOf(request));
    }

    @PostMapping("funcs/isNarrowerConceptOf")
    public ResponseEntity<TypeCheckResponse> isNarrowerConceptOf(@Valid @RequestBody TypeCheckRequest request) {
        return ResponseEntity.ok(entityService.isNarrowerConceptOf(request));
    }

    @PostMapping("funcs/getPrefLabel")
    public ResponseEntity<PrefLabelResponse> getPrefLabel(@Valid @RequestBody PrefLabelRequest request) {
        return ResponseEntity.ok(entityService.getPrefLabel(request));
    }

    @PostMapping("funcs/getDepiction")
    public ResponseEntity<DepictionResponse> getDepiction(@Valid @RequestBody DepictionRequest request) {
        return ResponseEntity.ok(entityService.getDepiction(request));
    }

    @PostMapping("funcs/getThumbnail")
    public ResponseEntity<ThumbnailResponse> getThumbnail(@Valid @RequestBody ThumbnailRequest request) {
        return ResponseEntity.ok(entityService.getThumbnail(request));
    }

    @PostMapping("funcs/getAbstract")
    public ResponseEntity<AbstractResponse> getAbstract(@Valid @RequestBody AbstractRequest request) {
        return ResponseEntity.ok(entityService.getAbstract(request));
    }

    @PostMapping("funcs/getSummary")
    public ResponseEntity<EntitySummaryResponse> getSummary(@Valid @RequestBody EntitySummaryRequest request) {
        return ResponseEntity.ok(entityService.getSummary(request));
    }
}
