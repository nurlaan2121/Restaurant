package restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import restaurant.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    default boolean existByEmail(String email) {
        User user = findByEmail(email).orElse(null);
        return user == null;
    }

    @Query("select rus from Restaurant r join r.users rus where r.id = :restId and rus.role = 'WAITER' or rus.role = 'CHEF'")
    Page<User> findAllByRestaurantId(Pageable pageable, Long restId);

    @Query("select u from User u join u.cheques uch where uch.id = :id")
    User findByCheckId(Long id);
}
