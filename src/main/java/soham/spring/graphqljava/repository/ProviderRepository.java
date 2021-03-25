package soham.spring.graphqljava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soham.spring.graphqljava.entity.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {

}
