package restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

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
}
