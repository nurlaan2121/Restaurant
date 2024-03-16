package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.entities.Restaurant;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {
    @Query("select r from Restaurant r where r.admin.email = :emailAdmin")
    Restaurant getRestWithAdmin(String emailAdmin);
}
