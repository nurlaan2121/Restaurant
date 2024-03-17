package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.entities.SubCategory;

import java.util.List;

public interface SubCategoryRepo extends JpaRepository<SubCategory,Long> {
    @Query("select sbc from Category c join c.subCategories sbc order by sbc.name")
    List<SubCategory> sortByName(Long catId);
    @Query("select sbc from SubCategory sbc join sbc.menuitemList")
    List<SubCategory> getByMenuitemId();
}
