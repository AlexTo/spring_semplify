package ai.semplify.entityhub.components;

import ai.semplify.entityhub.config.PoolParty;
import ai.semplify.entityhub.config.SPARQLEndpoint;
import ai.semplify.entityhub.config.Spotlight;
import lombok.var;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class Beans {

    private final PoolParty poolParty;
    private final Spotlight spotlight;
    private final SPARQLEndpoint sparqlEndpoint;

    public Beans(PoolParty poolParty, Spotlight spotlight, SPARQLEndpoint sparqlEndpoint) {
        this.poolParty = poolParty;
        this.spotlight = spotlight;
        this.sparqlEndpoint = sparqlEndpoint;
    }

    @Bean
    public Repository repository() {
        var repo = new SPARQLRepository(sparqlEndpoint.getUrl());
        if (sparqlEndpoint.isAuth()) {
            repo.setUsernameAndPassword(sparqlEndpoint.getUsername(), sparqlEndpoint.getPassword());
        }
        return repo;
    }
}
