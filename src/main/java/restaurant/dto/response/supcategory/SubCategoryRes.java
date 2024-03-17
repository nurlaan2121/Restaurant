package restaurant.dto.response.supcategory;

import lombok.*;
import restaurant.entities.Menuitem;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryRes {
    private Long id;
    private String name;
    private List<Menuitem> menuitemList;
}
