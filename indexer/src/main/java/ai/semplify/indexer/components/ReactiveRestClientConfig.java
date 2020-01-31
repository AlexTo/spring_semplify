package ai.semplify.indexer.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ReactiveRestClientConfig extends AbstractReactiveElasticsearchConfiguration {

    private ElasticsearchProperties properties;

    public ReactiveRestClientConfig(ElasticsearchProperties properties) {
        this.properties = properties;
    }


    @Override
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder() //
                .connectedTo(properties.getClusterNodes())
                .withWebClientConfigurer(webClient -> {
                    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs()
                                    .maxInMemorySize(-1))
                            .build();
                    return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
                })
                .build();
        return ReactiveRestClients.create(clientConfiguration);

    }
}
