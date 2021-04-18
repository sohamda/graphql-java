package soham.spring.graphqljava.service;


import org.springframework.beans.factory.annotation.Autowired;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.entity.Service;
import soham.spring.graphqljava.errors.NoDataFoundError;
import soham.spring.graphqljava.repository.ProviderRepository;
import soham.spring.graphqljava.repository.ServiceRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ProviderRepository providerRepository;


    public List<Service> findAll() {

        return serviceRepository.findAll();
    }

    public Service findById(String serviceId) {

        Optional<Service> service = serviceRepository.findById(Integer.parseInt(serviceId));

        if(service.isEmpty()) {
            throw new NoDataFoundError("No Service found", "SER-002");
        }
        return service.get();
    }

    public List<Service> findAllByProviderId(Integer providerId) {
        return serviceRepository.findAllByProviderId(providerId);
    }

    public Service addService(String name, String description, String providerId) {
        Integer maxId = serviceRepository.maxId();

        return serviceRepository.save(Service.builder().id(maxId+1)
                .name(name).description(description).providerId(Integer.parseInt(providerId))
                .build());
    }
}
