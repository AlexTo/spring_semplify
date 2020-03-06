package ai.semplify.commons.webclients;

import ai.semplify.commons.models.entityhub.Annotation;

public interface EntityHubWebClient {
    Annotation nerAnnotateServerFile(Long fileId);
}
