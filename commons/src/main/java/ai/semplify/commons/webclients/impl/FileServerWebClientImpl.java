package ai.semplify.commons.webclients.impl;

import ai.semplify.commons.serviceurls.fileserver.FileServerServiceUris;
import ai.semplify.commons.webclients.FileServerWebClient;
import lombok.var;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class FileServerWebClientImpl implements FileServerWebClient {

    private WebClient loadBalancedWebClient;

    public FileServerWebClientImpl(@Qualifier("loadBalancedWebClient") WebClient loadBalancedWebClient) {
        this.loadBalancedWebClient = loadBalancedWebClient;
    }


    @Override
    public byte[] download(Long fileId) {
        var url = String.format(FileServerServiceUris.FILES_DOWNLOAD, fileId);
        loadBalancedWebClient
                .get().uri(url)
                .attributes(clientRegistrationId("service-client"))
                .exchange()               .block();
    }
}
