package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.entities.Cheque;

import java.math.BigDecimal;
import java.util.List;

public interface ChequeRepo extends JpaRepository<Cheque,Long> {
    @Query("select c from User u join u.cheques c where u.id = :userId")
    List<Cheque> findAllByUserId(Long userId);
    @Query("select avg (rusch.priceAverage) from Restaurant r join r.users rus join rus.cheques rusch where r.id = :id")
    BigDecimal getAvg(Long id);
}
