package soham.spring.graphqljava.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="service")
public class Service {

    @Id
    private Integer id;
    private String name;
    private String description;
    @Column(name = "provider_id")
    private Integer providerId;

}
