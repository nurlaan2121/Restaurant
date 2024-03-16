package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.Category;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
