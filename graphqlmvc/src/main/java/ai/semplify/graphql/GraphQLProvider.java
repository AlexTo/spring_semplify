package ai.semplify.graphql;

import ai.semplify.graphql.resolvers.Mutation;
import ai.semplify.graphql.resolvers.Query;
import com.coxautodev.graphql.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GraphQLProvider {

    private final Query query;
    private final Mutation mutation;


    public GraphQLProvider(Query query,
                           Mutation mutation) {
        this.query = query;
        this.mutation = mutation;
    }

    @Bean
    public GraphQLSchema graphQLSchema() {
        return SchemaParser.newParser()
                .file("graphql/schema.graphqls")
                .resolvers(query, mutation)
                .build()
                .makeExecutableSchema();
    }
}
