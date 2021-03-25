package soham.spring.graphqljava.datafetcher;

import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.entity.Service;
import soham.spring.graphqljava.repository.ServiceRepository;

@Component
public class ServiceDataFetcher {

    @Autowired
    ServiceRepository serviceRepository;


    public DataFetcher getAllServices() {
        return dataFetchingEnvironment -> serviceRepository.findAll();
    }

    public DataFetcher getServiceById() {
        return dataFetchingEnvironment -> {
            String serviceId = dataFetchingEnvironment.getArgument("id");
            return serviceRepository.findById(Integer.parseInt(serviceId));
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
            return serviceRepository.findAllByProvider(provider);
        };
    }
}
