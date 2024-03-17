package restaurant.entities;
import jakarta.persistence.*;
import lombok.*;
import restaurant.dto.response.category.CategoryPagination;
import restaurant.dto.response.category.CategoryRes;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "category")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "cat_id", allocationSize = 1)
public class Category extends BaseEntity {
    private String name;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<SubCategory> subCategories = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public CategoryRes convert() {
        return new CategoryRes(name,subCategories);
    }
}
