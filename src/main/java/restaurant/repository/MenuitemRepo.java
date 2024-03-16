package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.Menuitem;

public interface MenuitemRepo extends JpaRepository<Menuitem,Long> {
}
