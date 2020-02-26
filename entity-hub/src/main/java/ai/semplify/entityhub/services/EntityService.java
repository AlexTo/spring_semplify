package ai.semplify.entityhub.services;

import ai.semplify.entityhub.models.*;

public interface EntityService {
    TypeCheckResponse isSubClassOf(TypeCheckRequest request);

    TypeCheckResponse isNarrowerConceptOf(TypeCheckRequest request);

    PrefLabelResponse getPrefLabel(PrefLabelRequest request);

    PrefLabelResponse getPrefLabel(String uri);

    DepictionResponse getDepiction(DepictionRequest request);

    DepictionResponse getDepiction(String uri);

    ThumbnailResponse getThumbnail(ThumbnailRequest request);

    ThumbnailResponse getThumbnail(String uri);

    AbstractResponse getAbstract(AbstractRequest request);

    AbstractResponse getAbstract(String uri);

    EntitySummaryResponse getSummary(String uri);

    EntitySummaryResponse getSummary(EntitySummaryRequest request);

    void invalidateCache(String uri);

}
