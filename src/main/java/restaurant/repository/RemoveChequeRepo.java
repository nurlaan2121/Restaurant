package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.RemoveUsersCheques;

public interface RemoveChequeRepo extends JpaRepository<RemoveUsersCheques,Long>{
}
