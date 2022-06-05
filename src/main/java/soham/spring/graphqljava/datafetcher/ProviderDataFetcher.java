package soham.spring.graphqljava.datafetcher;

import graphql.schema.DataFetcher;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.service.ProviderService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ProviderDataFetcher {

    @Autowired
    ProviderService providerService;

    public DataFetcher<List<Provider>> getAllProviders() {
        return environment -> providerService.findAllProviders();
    }

    public DataFetcher<Provider> getProviderById() {
        return environment -> {
            String providerId = environment.getArgument("id");
            return providerService.findProviderByID(providerId);
        };
    }

    public BatchLoader<Integer, Provider> providerBatchLoader() {
        return keys ->
                CompletableFuture.supplyAsync(() -> providerService.findByIdIn(keys));
    }

    public DataFetcher<Provider> addProvider() {
        return environment -> {
            String name = environment.getArgument("name");
            String description = environment.getArgument("description");
            return providerService.addProvider(name, description);
        };
    }
}
