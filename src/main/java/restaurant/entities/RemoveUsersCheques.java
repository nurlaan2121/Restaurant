package restaurant.entities;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "remove_cheques")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "rem_cheque_id",allocationSize = 1)
public class RemoveUsersCheques extends BaseEntity {
    private String waiterEmail;
    private BigDecimal priceAverage;
    private ZonedDateTime createdAdCheque;
    private String menuitemList;
}
