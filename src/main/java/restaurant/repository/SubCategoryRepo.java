package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.SubCategory;

public interface SubCategoryInterface extends JpaRepository<SubCategory,Long> {
}
