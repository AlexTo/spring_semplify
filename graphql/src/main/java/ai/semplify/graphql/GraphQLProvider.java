package ai.semplify.graphql;

import ai.semplify.graphql.resolvers.Query;
import ai.semplify.graphql.resolvers.Subscription;
import com.coxautodev.graphql.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GraphQLProvider {

    private Subscription subscription;
    private Query query;

    public GraphQLProvider(Subscription subscription, Query query) {
        this.subscription = subscription;
        this.query = query;
    }

    @Bean
    public GraphQLSchema graphQLSchema() {

        return SchemaParser.newParser()
                .file("graphql/schema.graphqls")
                .resolvers(query, subscription)
                .build()
                .makeExecutableSchema();
    }
}
