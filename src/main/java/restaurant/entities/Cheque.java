package restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cheques")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "cheque_id",allocationSize = 1)
public class Cheque extends BaseEntity{
    private BigDecimal priceAverage;
    private ZonedDateTime createdAdCheque;
    @ManyToMany
    private List<Menuitem> menuitemList = new ArrayList<>();
}
