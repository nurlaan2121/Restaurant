package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.StopList;

public interface StopListRepo extends JpaRepository<StopList,Long> {
}
