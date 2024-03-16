package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.User;

public interface UserInterface extends JpaRepository<User,Long> {
}
