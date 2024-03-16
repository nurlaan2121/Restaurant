package restaurant.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "cat_id",allocationSize = 1)
public class Category extends BaseEntity{
    private String name;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<SubCategory> subCategories = new ArrayList<>();
}
