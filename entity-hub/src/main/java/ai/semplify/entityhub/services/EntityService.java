package ai.semplify.entityhub.services;

import ai.semplify.entityhub.models.TypeCheckRequest;
import ai.semplify.entityhub.models.TypeCheckResponse;

public interface EntityService {
    TypeCheckResponse isOfType(TypeCheckRequest request);

    TypeCheckResponse isSubClassOfTransitive(TypeCheckRequest request);
}
