package soham.spring.graphqljava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soham.spring.graphqljava.entity.Provider;
import soham.spring.graphqljava.errors.NoDataFoundError;
import soham.spring.graphqljava.repository.ProviderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    public List<Provider> findAllProviders() {
        List<Provider> providers =  providerRepository.findAll();

        if(providers.isEmpty()) {
            throw new NoDataFoundError("No Providers found", "PRV-001");
        }
        return providers;
    }

    public Provider findProviderByID(String providerId) {
        Optional<Provider> provider =  providerRepository.findById(Integer.parseInt(providerId));

        if(provider.isEmpty()) {
            throw new NoDataFoundError("No Provider found", "PRV-002");
        }
        return provider.get();
    }

    public List<Provider> findByIdIn(List<Integer> ids) {
        return providerRepository.findByIdIn(ids);
    }

    public Provider addProvider(String name, String description) {
        Integer maxId = providerRepository.maxId();

        return providerRepository.save(Provider.builder().id(maxId+1).name(name).description(description).build());
    }
}
