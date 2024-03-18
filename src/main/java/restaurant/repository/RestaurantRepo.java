package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.dto.response.menuitem.MenuitemRes;
import restaurant.entities.Restaurant;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {
    @Query("select r from Restaurant r where r.admin.email = :emailAdmin")
    Restaurant getRestWithAdmin(String emailAdmin);

    @Query("select r from Restaurant  r join r.users rus where rus.id = :id")
    Restaurant getRestWithChef(Long id);
    @Query("select new restaurant.dto.response.menuitem.MenuitemRes(rml.id,rml.name,rml.image,rml.price,rml.description,rml.isVegetarian,rml.count) from Restaurant r join r.menuitemList rml where r.id = :id order by rml.price")
    List<MenuitemRes> sortByPrice(Long id);
    @Query("select new restaurant.dto.response.menuitem.MenuitemRes(rml.id,rml.name,rml.image,rml.price,rml.description,rml.isVegetarian,rml.count) from Restaurant r join r.menuitemList rml where r.id = :id order by rml.price desc")
    List<MenuitemRes> sortByPriceDesc(Long id);
    @Query("select new restaurant.dto.response.menuitem.MenuitemRes(rml.id,rml.name,rml.image,rml.price,rml.description,rml.isVegetarian,rml.count) from Restaurant r join r.menuitemList rml where r.id = :id and rml.isVegetarian = :isVeg")
    List<MenuitemRes> filterByVega(Long id, boolean isVeg);
    @Query("select r from Restaurant r join r.users rus where rus.email = :email")
    Restaurant getRestWithWaiter(String email);
}
