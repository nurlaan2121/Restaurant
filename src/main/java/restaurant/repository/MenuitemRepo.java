package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.dto.response.menuitem.MenuitemRes;
import restaurant.entities.Menuitem;

import java.util.List;

public interface MenuitemRepo extends JpaRepository<Menuitem,Long> {
    @Query("select new restaurant.dto.response.menuitem.MenuitemRes(rml.id," +
            "rml.name,rml.image,rml.price,rml.description,rml.isVegetarian,rml.count) from Restaurant r join " +
            "r.menuitemList rml where r.id =:restId and rml.name ilike :word")
    List<MenuitemRes> search(String word, Long restId);
}
