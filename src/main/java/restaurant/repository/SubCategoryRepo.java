package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.dto.response.supcategory.SubCategoryRes;
import restaurant.entities.SubCategory;

import java.util.List;

public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {
    @Query("select sbc from Category c join c.subCategories sbc order by sbc.name")
    List<SubCategory> sortByName(Long catId);

    @Query("select sbc from SubCategory sbc join sbc.menuitemList")
    List<SubCategory> getByMenuitemId();

    @Query("select new restaurant.dto.response.supcategory.SubCategoryRes(scs.id,scs.name,scs.menuitemList) FROM Category c join c.subCategories scs where c.id = :catId and scs.name ilike (:word)")
    List<SubCategoryRes> search(String word, Long catId);

    default boolean existsByName(String name, Long cateId) {
        return existsByCatId(name, cateId);
    }

    @Query("select case when count(scs) > 0 then true else false end " +
            "from Category c join c.subCategories scs " +
            "where scs.name = :name and c.id = :categoryId")
    boolean existsByCatId(String name, Long categoryId);
}
