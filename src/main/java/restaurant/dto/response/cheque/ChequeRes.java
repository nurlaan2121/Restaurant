package restaurant.dto.response.cheque;

import lombok.*;
import restaurant.entities.Menuitem;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChequeRes {
    private String waiterFullName;
    private List<Menuitem> menuitemList;
    private BigDecimal price;
    private Double servicePro;
    private BigDecimal totalPrice;

    public ChequeRes(List<Menuitem> menuitemList,BigDecimal price) {
        this.menuitemList = menuitemList;
        this.price = price;
    }
}
