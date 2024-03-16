package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.SubCategory;

public interface SubCategoryRepo extends JpaRepository<SubCategory,Long> {
}
