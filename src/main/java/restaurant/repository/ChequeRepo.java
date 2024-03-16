package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.entities.Cheque;

public interface ChequeInterface extends JpaRepository<Cheque,Long> {
}
