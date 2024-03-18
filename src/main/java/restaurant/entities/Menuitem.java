package restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;
import restaurant.dto.response.menuitem.MenuitemRes;

import java.math.BigDecimal;

@Entity
@Table(name = "menuitems")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "menuitem_id",allocationSize = 1)
@ToString
public class Menuitem extends BaseEntity{
    private String name;
    private String image;
    private BigDecimal price;
    private String description;
    private boolean isVegetarian;
    private Long count;

    public MenuitemRes convert() {
        return new MenuitemRes(super.getId(),this.name,this.image,this.price,this.description,this.isVegetarian,this.count);
    }

}
