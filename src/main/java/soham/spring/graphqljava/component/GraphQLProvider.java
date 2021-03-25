package soham.spring.graphqljava.component;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.datafetcher.ProviderDataFetcher;
import soham.spring.graphqljava.datafetcher.ServiceDataFetcher;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    private GraphQL graphQL;

    @Autowired
    ProviderDataFetcher providerDataFetcher;
    @Autowired
    ServiceDataFetcher serviceDataFetcher;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        Resource resource = new ClassPathResource("schema/schema.graphqls");
        String sdl = new String(resource.getInputStream().readAllBytes());
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("services", serviceDataFetcher.getAllServices())
                        .dataFetcher("serviceById", serviceDataFetcher.getServiceById())
                        .dataFetcher("providers", providerDataFetcher.getAllProviders())
                        .dataFetcher("providerById", providerDataFetcher.getProviderById()))
                .type(newTypeWiring("Service")
                        .dataFetcher("provider", serviceDataFetcher.getProviderForService()))
                .type(newTypeWiring("Provider")
                        .dataFetcher("services", serviceDataFetcher.getServicesForProvider()))
                .build();
    }

}
