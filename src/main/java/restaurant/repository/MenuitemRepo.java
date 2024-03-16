package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.Menuitem;

public interface MenuitemInterface extends JpaRepository<Menuitem,Long> {
}
