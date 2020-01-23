package ai.semplify.entityhub.components;

import ai.semplify.entityhub.config.PoolParty;
import ai.semplify.entityhub.config.Spotlight;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServerBearerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Beans {

    private final PoolParty poolParty;
    private final Spotlight spotlight;

    public Beans(PoolParty poolParty, Spotlight spotlight) {
        this.poolParty = poolParty;
        this.spotlight = spotlight;
    }

    @Bean
    @LoadBalanced
    WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean(name = "loadBalancedWebClient")
    public WebClient loadBalancedWebClient(@LoadBalanced WebClient.Builder loadBalancedWebClientBuilder) {
        return loadBalancedWebClientBuilder
                .filter(new ServerBearerExchangeFilterFunction())
                .build();
    }

    @Bean(name = "poolpartyWebClient")
    public WebClient poolpartyWebClient() {
        return WebClient.builder()
                .defaultHeaders(httpHeaders ->
                        httpHeaders.setBasicAuth(poolParty.getUsername(), poolParty.getPassword()))
                .baseUrl(String.format("http://%s:%d", poolParty.getHost(), poolParty.getPort()))
                .build();
    }

    @Bean(name = "spotlightWebClient")
    public WebClient spotlightWebClient() {
        return WebClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                    httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .baseUrl(String.format("http://%s:%d", spotlight.getHost(), spotlight.getPort()))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }
}
