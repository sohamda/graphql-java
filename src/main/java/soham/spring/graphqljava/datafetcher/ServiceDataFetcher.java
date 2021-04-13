package soham.spring.graphqljava.datafetcher;

import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.entity.Service;
import soham.spring.graphqljava.errors.NoDataFoundError;
import soham.spring.graphqljava.repository.ServiceRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ServiceDataFetcher {

    @Autowired
    ServiceRepository serviceRepository;


    public DataFetcher getAllServices() {
        return dataFetchingEnvironment -> {

            List<Service> services = serviceRepository.findAll();
            return DataFetcherResult.newResult().data(services).build();
        };
    }

    public DataFetcher getServiceById() {
        return dataFetchingEnvironment -> {
            String serviceId = dataFetchingEnvironment.getArgument("id");
            Optional<Service> service = serviceRepository.findById(Integer.parseInt(serviceId));

            if(service.isEmpty()) {
                throw new NoDataFoundError("No Service found", "SER-002");
            }

            return DataFetcherResult.newResult().data(service).build();
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
            List<Service> services = serviceRepository.findAllByProviderId(provider.getId());
            return DataFetcherResult.newResult().data(services).build();
        };
    }
}
