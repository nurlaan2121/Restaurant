package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.StopList;

public interface StopListInterface extends JpaRepository<StopList,Long> {
}
