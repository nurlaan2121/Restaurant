package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.entities.Category;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    @Query("select rcs from Restaurant r join r.categories rcs where r.id = :id")
    Category getCatByResId(Long id);
}
