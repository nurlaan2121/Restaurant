package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.dto.response.category.CategoryRes;
import restaurant.entities.Category;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query("select rcs from Restaurant r join r.categories rcs where r.id = :id")
    Category getCatByResId(Long id);

    @Query("select new restaurant.dto.response.category.CategoryRes(rcs.id, rcs.name, count(s)) " +
            "from Restaurant r " +
            "left join r.categories rcs " +
            "left join rcs.subCategories s " +
            "where rcs.name ilike (:word) and r.id = :restId " +
            "group by rcs.id, rcs.name")
    List<CategoryRes> search(String word, Long restId);

}
