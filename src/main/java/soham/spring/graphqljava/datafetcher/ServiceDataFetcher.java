package soham.spring.graphqljava.datafetcher;

import graphql.GraphQLError;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.entity.Service;
import soham.spring.graphqljava.errors.NoDataFoundError;
import soham.spring.graphqljava.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ServiceDataFetcher {

    @Autowired
    ServiceRepository serviceRepository;


    public DataFetcher getAllServices() {
        return dataFetchingEnvironment -> {

            List<Service> services = serviceRepository.findAll();

            List<GraphQLError> errorList = new ArrayList<>();
            if(services.isEmpty()) {
                errorList.add(new NoDataFoundError("No Services found", "SER-001"));
            }

            return DataFetcherResult.newResult().data(services).errors(errorList).build();
        };
    }

    public DataFetcher getServiceById() {
        return dataFetchingEnvironment -> {
            String serviceId = dataFetchingEnvironment.getArgument("id");
            Optional<Service> service = serviceRepository.findById(Integer.parseInt(serviceId));

            List<GraphQLError> errorList = new ArrayList<>();
            if(service.isEmpty()) {
                errorList.add(new NoDataFoundError("No Service found", "SER-002"));
            }

            return DataFetcherResult.newResult().data(service).errors(errorList).build();
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

            List<GraphQLError> errorList = new ArrayList<>();
            if(services.isEmpty()) {
                errorList.add(new NoDataFoundError("No Services found", "SER-001"));
            }

            return DataFetcherResult.newResult().data(services).errors(errorList).build();
        };
    }
}
