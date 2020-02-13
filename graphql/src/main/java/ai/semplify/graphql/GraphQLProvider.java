package ai.semplify.graphql;

import ai.semplify.graphql.resolvers.MutationResolver;
import ai.semplify.graphql.resolvers.QueryResolver;
import com.coxautodev.graphql.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GraphQLProvider {

    private final QueryResolver queryResolver;
    private final MutationResolver mutationResolver;


    public GraphQLProvider(QueryResolver queryResolver,
                           MutationResolver mutationResolver) {
        this.queryResolver = queryResolver;
        this.mutationResolver = mutationResolver;
    }

    @Bean
    public GraphQLSchema graphQLSchema() {
        return SchemaParser.newParser()
                .file("graphql/schema.graphqls")
                .resolvers(queryResolver, mutationResolver)
                .build()
                .makeExecutableSchema();
    }
}
