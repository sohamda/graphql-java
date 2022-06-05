package soham.spring.graphqljava.datafetcher;

import graphql.schema.DataFetcher;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.entity.Service;
import soham.spring.graphqljava.service.ServicesService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ServiceDataFetcher {

    @Autowired
    ServicesService servicesService;


    public DataFetcher<List<Service>> getAllServices() {
        return dataFetchingEnvironment -> servicesService.findAll();
    }

    public DataFetcher<Service> getServiceById() {
        return dataFetchingEnvironment -> {
            String serviceId = dataFetchingEnvironment.getArgument("id");
            return servicesService.findById(serviceId);
        };
    }

    public DataFetcher<CompletableFuture<Provider>> getProviderForService() {
        return dataFetchingEnvironment -> {
            Service service = dataFetchingEnvironment.getSource();

            DataLoader<Integer, Provider> providerDataLoader = dataFetchingEnvironment.getDataLoader("providers");
            return providerDataLoader.load(service.getProviderId());
        };
    }

    public DataFetcher<List<Service>> getServicesForProvider() {
        return dataFetchingEnvironment -> {
            Provider provider = dataFetchingEnvironment.getSource();
            return servicesService.findAllByProviderId(provider.getId());
        };
    }

    public DataFetcher<Service> addService() {
        return dataFetchingEnvironment -> {
            String name = dataFetchingEnvironment.getArgument("name");
            String description = dataFetchingEnvironment.getArgument("description");
            String providerId = dataFetchingEnvironment.getArgument("providerId");

            return servicesService.addService(name, description, providerId);
        };
    }
}
