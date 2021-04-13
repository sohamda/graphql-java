package soham.spring.graphqljava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soham.spring.graphqljava.entity.Service;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

    public List<Service> findAllByProviderId(Integer providerId);
}
