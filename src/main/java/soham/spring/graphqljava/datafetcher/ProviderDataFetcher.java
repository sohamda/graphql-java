package soham.spring.graphqljava.datafetcher;

import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.errors.NoDataFoundError;
import soham.spring.graphqljava.repository.ProviderRepository;

import java.util.List;
import java.util.Optional;

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
}
