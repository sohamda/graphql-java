package soham.spring.graphqljava.datafetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.service.ProviderService;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class ProviderDataFetcher {

    @Autowired
    ProviderService providerService;

    public DataFetcher getAllProviders() {
        return dataFetchingEnvironment -> {
            return DataFetcherResult.newResult().data(providerService.findAllProviders()).build();
        };
    }

    public DataFetcher getProviderById() {
        return dataFetchingEnvironment -> {
            String providerId = dataFetchingEnvironment.getArgument("id");
            return DataFetcherResult.newResult().data(providerService.findProviderByID(providerId)).build();
        };
    }

    public BatchLoader<Integer, Provider> providerBatchLoader() {
        final BatchLoader<Integer, Provider> batchLoader = keys -> CompletableFuture.supplyAsync(() -> providerService.findByIdIn(keys));
        return batchLoader;
    }

    public DataFetcher addProvider() {
        return dataFetchingEnvironment -> {
            String name = dataFetchingEnvironment.getArgument("name");
            String description = dataFetchingEnvironment.getArgument("description");

            return DataFetcherResult.newResult().data(providerService.addProvider(name, description)).build();
        };
    }
}
