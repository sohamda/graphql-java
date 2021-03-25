package soham.spring.graphqljava.datafetcher;

import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.entity.Service;
import soham.spring.graphqljava.repository.ProviderRepository;
import soham.spring.graphqljava.repository.ServiceRepository;

@Component
public class ProviderDataFetcher {

    @Autowired
    ProviderRepository providerRepository;

    public DataFetcher getAllProviders() {
        return dataFetchingEnvironment -> {
            return providerRepository.findAll();
        };
    }

    public DataFetcher getProviderById() {
        return dataFetchingEnvironment -> {
            String providerId = dataFetchingEnvironment.getArgument("id");
            return providerRepository.findById(Integer.parseInt(providerId));
        };
    }
}
