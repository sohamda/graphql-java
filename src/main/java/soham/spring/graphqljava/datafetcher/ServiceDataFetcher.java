package soham.spring.graphqljava.datafetcher;

import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.entity.Service;
import soham.spring.graphqljava.service.ServicesService;

@Component
public class ServiceDataFetcher {

    @Autowired
    ServicesService servicesService;


    public DataFetcher getAllServices() {
        return dataFetchingEnvironment -> {
            return DataFetcherResult.newResult().data(servicesService.findAll()).build();
        };
    }

    public DataFetcher getServiceById() {
        return dataFetchingEnvironment -> {
            String serviceId = dataFetchingEnvironment.getArgument("id");
            return DataFetcherResult.newResult().data(servicesService.findById(serviceId)).build();
        };
    }

    public DataFetcher getProviderForService() {
        return dataFetchingEnvironment -> {
            Service service = dataFetchingEnvironment.getSource();

            DataLoader<Integer, Provider> providerDataLoader = dataFetchingEnvironment.getDataLoader("providers");
            return providerDataLoader.load(service.getProviderId());
        };
    }

    public DataFetcher getServicesForProvider() {
        return dataFetchingEnvironment -> {
            Provider provider = dataFetchingEnvironment.getSource();
            return DataFetcherResult.newResult().data(servicesService.findAllByProviderId(provider.getId())).build();
        };
    }

    public DataFetcher addService() {
        return dataFetchingEnvironment -> {
            String name = dataFetchingEnvironment.getArgument("name");
            String description = dataFetchingEnvironment.getArgument("description");
            String providerId = dataFetchingEnvironment.getArgument("providerId");

            return DataFetcherResult.newResult().data(servicesService.addService(name, description, providerId)).build();
        };
    }
}
