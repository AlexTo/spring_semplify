package ai.semplify.entityhub.services;

import ai.semplify.entityhub.models.PrefLabelRequest;
import ai.semplify.entityhub.models.PrefLabelResponse;
import ai.semplify.entityhub.models.TypeCheckRequest;
import ai.semplify.entityhub.models.TypeCheckResponse;

public interface EntityService {
    TypeCheckResponse isSubClassOf(TypeCheckRequest request);

    TypeCheckResponse isNarrowerConceptOf(TypeCheckRequest request);

    PrefLabelResponse getPrefLabel(PrefLabelRequest request);

    PrefLabelResponse getPrefLabel(String uri);
}
