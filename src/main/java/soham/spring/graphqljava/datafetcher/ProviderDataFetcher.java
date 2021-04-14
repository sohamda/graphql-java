package soham.spring.graphqljava.datafetcher;

import graphql.execution.DataFetcherResult;
import graphql.relay.*;
import graphql.schema.DataFetcher;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.errors.NoDataFoundError;
import soham.spring.graphqljava.repository.ProviderRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class ProviderDataFetcher {

    @Autowired
    ProviderRepository providerRepository;

    public DataFetcher getAllProviders() {
        return dataFetchingEnvironment -> {
            List<Provider> providers =  providerRepository.findAll();

            if(providers.isEmpty()) {
                throw new NoDataFoundError("No Providers found", "PRV-001");
            }

            return DataFetcherResult.newResult().data(providers).build();
        };
    }

    public DataFetcher getProviderById() {
        return dataFetchingEnvironment -> {
            String providerId = dataFetchingEnvironment.getArgument("id");
            Optional<Provider> provider =  providerRepository.findById(Integer.parseInt(providerId));

            if(provider.isEmpty()) {
                throw new NoDataFoundError("No Provider found", "PRV-002");
            }

            return DataFetcherResult.newResult().data(provider).build();
        };
    }

    public BatchLoader<Integer, Provider> providerBatchLoader() {
        final BatchLoader<Integer, Provider> batchLoader = keys -> CompletableFuture.supplyAsync(() -> providerRepository.findByIdIn(keys));
        return batchLoader;
    }

    public DataFetcher<Connection<Provider>> getProviderPaginated() {
        return dataFetchingEnvironment -> {
            Integer pageNo = dataFetchingEnvironment.getArgument("pageNo");
            Integer numberOfElements = dataFetchingEnvironment.getArgument("numberOfElements");
            Pageable pageable = PageRequest.of(pageNo, numberOfElements);

            List edges = providerRepository.findAll(pageable)
                    .stream()
                    .map(provider -> new DefaultEdge(provider, new DefaultConnectionCursor("asd")))
                    .limit(numberOfElements)
                    .collect(Collectors.toList());

            PageInfo pageInfo = new DefaultPageInfo(
                    new DefaultConnectionCursor("asd"),
                    new DefaultConnectionCursor("asd"),
                    pageNo != 0,
                    edges.size() >= numberOfElements);

            return new DefaultConnection<>(edges, pageInfo);
            //return DataFetcherResult.newResult().data(providerId).build();
        };
    }
}
