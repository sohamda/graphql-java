package soham.spring.graphqljava.datafetcher;

import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.repository.ProviderRepository;

@Component
public class ProviderDataFetcher {

    @Autowired
    ProviderRepository providerRepository;

    public DataFetcher getAllProviders() {
        return dataFetchingEnvironment -> providerRepository.findAll();
    }

    public DataFetcher getProviderById() {
        return dataFetchingEnvironment -> {
            String providerId = dataFetchingEnvironment.getArgument("id");
            return providerRepository.findById(Integer.parseInt(providerId));
        };
    }
}
