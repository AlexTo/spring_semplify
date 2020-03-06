package ai.semplify.commons.webclients.impl;

import ai.semplify.commons.models.entityhub.Annotation;
import ai.semplify.commons.serviceurls.entityhub.EntityHubServiceUris;
import ai.semplify.commons.webclients.EntityHubWebClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class EntityHubWebClientImpl implements EntityHubWebClient {

    private WebClient loadBalancedWebClient;

    public EntityHubWebClientImpl(@Qualifier("loadBalancedWebClient") WebClient loadBalancedWebClient) {
        this.loadBalancedWebClient = loadBalancedWebClient;
    }

    @Override
    public Annotation nerAnnotateServerFile(Long fileId) {
        return loadBalancedWebClient
                .post().uri(EntityHubServiceUris.NER_ANNOTATE_SERVER_FILE)
                .bodyValue(fileId)
                .attributes(clientRegistrationId("service-client"))
                .retrieve().bodyToMono(Annotation.class).block();
    }
}
