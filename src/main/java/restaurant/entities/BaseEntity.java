package restaurant.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "id_gen")
    private Long id;
    private LocalDate createdAd;
    private LocalDate updateAd;

    @PrePersist
    private void preCreated(){
        this.createdAd = LocalDate.now();
    }
    @PreUpdate
    private void preUpdate(){
        this.updateAd = LocalDate.now();
    }
}
