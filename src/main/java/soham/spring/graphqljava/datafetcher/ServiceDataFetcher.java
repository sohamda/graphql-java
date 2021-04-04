package soham.spring.graphqljava.datafetcher;

import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
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

            if(services.isEmpty()) {
                throw new NoDataFoundError("No Services found", "SER-001");
            }

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
            return service.getProvider();
        };
    }

    public DataFetcher getServicesForProvider() {
        return dataFetchingEnvironment -> {
            Provider provider = dataFetchingEnvironment.getSource();
            List<Service> services = serviceRepository.findAllByProvider(provider);

            if(services.isEmpty()) {
                throw new NoDataFoundError("No Services found", "SER-001");
            }

            return DataFetcherResult.newResult().data(services).build();
        };
    }
}
