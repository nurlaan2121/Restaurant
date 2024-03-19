package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.entities.Cheque;
import restaurant.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface ChequeRepo extends JpaRepository<Cheque, Long> {
    @Query("select c from User u join u.cheques c where u.id = :userId")
    List<Cheque> findAllByUserId(Long userId);

    default BigDecimal getAvg(Long id, LocalDate date) {
        return getAvg2(id, date.getDayOfMonth());
    }

    @Query("SELECT AVG(rusch.priceAverage) FROM Restaurant r JOIN r.users rus JOIN rus.cheques rusch WHERE r.id = :id AND DAY(rusch.createdAdCheque) = :dayOfMonth")
    BigDecimal getAvg2(Long id, int dayOfMonth);

    @Query("select uch from User u join u.cheques uch where u.id = :userId and uch.createdAdCheque = :date")
    List<Cheque> findByWaiterAndDate(ZonedDateTime date, Long userId);
    @Query("select c from Cheque c join c.menuitemList cml where cml.id = :id")
    Cheque getByMen(Long id);
}
