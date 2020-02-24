package ai.semplify.entityhub.services;

import ai.semplify.entityhub.models.*;

public interface EntityService {
    TypeCheckResponse isSubClassOf(TypeCheckRequest request);

    TypeCheckResponse isNarrowerConceptOf(TypeCheckRequest request);

    PrefLabelResponse getPrefLabel(PrefLabelRequest request);

    PrefLabelResponse getPrefLabel(String uri);

    DepictionResponse getDepiction(DepictionRequest request);

    DepictionResponse getDepiction(String uri);
}
