package restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;
import restaurant.dto.response.supcategory.SubCategoryRes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subCategories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "sub_cat_id",allocationSize = 1)
public class SubCategory extends BaseEntity{
    private String name;
    @OneToMany
    private List<Menuitem> menuitemList = new ArrayList<>();

    public SubCategory(String name) {
        this.name = name;
    }
    public SubCategoryRes convert(){
        return new SubCategoryRes(super.getId(),this.name,this.menuitemList);
    }
}
