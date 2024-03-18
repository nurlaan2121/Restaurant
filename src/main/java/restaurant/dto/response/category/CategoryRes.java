package restaurant.dto.response.category;

import lombok.*;
import restaurant.entities.SubCategory;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CategoryRes {
    private Long id;
    private String name;
    private List<SubCategory> subCategories = new ArrayList<>();
    private Long subCategoriesCount;

    public CategoryRes(String name, List<SubCategory> subCategories) {
        this.name = name;
        this.subCategories = subCategories;
    }

    public CategoryRes(Long id, String name, Long countSubCat) {
        this.id = id;
        this.name = name;
        this.subCategoriesCount = countSubCat;
    }
}
