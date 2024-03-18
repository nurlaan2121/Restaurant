package restaurant.dto.response.menuitem;
import lombok.*;
import java.math.BigDecimal;
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuitemRes{
    private Long id;
    private String name;
    private String image;
    private BigDecimal price;
    private String description;
    private boolean isVegetarian;
    private Long count;

}
