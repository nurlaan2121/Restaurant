package restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import restaurant.entities.Menuitem;
import restaurant.validation.NotNegative;
import restaurant.validation.NotNegativeBig;

import java.math.BigDecimal;
@Builder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuitemReq {
    @NotNegative
    private Long subCategory;
    @NotBlank
    private String name;
    @NotBlank
    private String image;
//    @NotNegativeBig
    private BigDecimal price;
    @NotBlank
    private String description;
    private boolean isVegetarian;
    @NotNegative
    private Long count;

    public Menuitem convert() {
        return new Menuitem(this.name,this.image,this.price,this.description,this.isVegetarian,this.count);
    }
}
