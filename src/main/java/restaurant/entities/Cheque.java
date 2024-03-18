package restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;
import restaurant.dto.response.cheque.ChequeRes;

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

    public ChequeRes convert() {
        return new ChequeRes(this.menuitemList,this.priceAverage);
    }
}
