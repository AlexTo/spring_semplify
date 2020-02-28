package ai.semplify.entityhub.components;

import ai.semplify.entityhub.config.SPARQLEndpointConfig;
import lombok.var;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SPARQL {

    private final SPARQLEndpointConfig sparqlEndpointConfig;

    public SPARQL(SPARQLEndpointConfig sparqlEndpointConfig) {
        this.sparqlEndpointConfig = sparqlEndpointConfig;
    }

    @Bean
    public Repository repository() {
        var repo = new SPARQLRepository(sparqlEndpointConfig.getUrl());
        if (sparqlEndpointConfig.isAuth()) {
            repo.setUsernameAndPassword(sparqlEndpointConfig.getUsername(), sparqlEndpointConfig.getPassword());
        }
        return repo;
    }
}
