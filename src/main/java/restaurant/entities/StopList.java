package restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "stop_lists")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "stop_list_id",allocationSize = 1)
public class StopList extends BaseEntity{
    private String reason;
    private ZonedDateTime date;
    @OneToOne
    private Menuitem menuitem;
}
