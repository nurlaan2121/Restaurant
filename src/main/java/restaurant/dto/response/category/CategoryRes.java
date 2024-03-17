package restaurant.dto.response.category;

import lombok.*;
import restaurant.entities.SubCategory;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRes {
    private Long id;
    private String name;
    private List<SubCategory> subCategories;

    public CategoryRes(String name, List<SubCategory> subCategories) {
        this.name = name;
        this.subCategories = subCategories;
    }
}
