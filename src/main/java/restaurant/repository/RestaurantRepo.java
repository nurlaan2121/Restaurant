package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.Restaurant;

public interface RestaurantInterface extends JpaRepository<Restaurant, Long> {

}
