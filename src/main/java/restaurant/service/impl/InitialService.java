package restaurant.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.entities.Restaurant;
import restaurant.entities.User;
import restaurant.enums.RestaurantType;
import restaurant.enums.Role;
import restaurant.repository.RestaurantRepo;
import restaurant.repository.UserRepo;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitialService {
    private final UserRepo userRepo;
    private final RestaurantRepo restaurantRepo;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct @Transactional
    protected void saveDev() {
        userRepo.save(new User("dev", "dev", LocalDate.of(2000, 04, 04), "dev@gmail.com", "+996777289688", Role.DEV, 15L, passwordEncoder.encode("Nurlan21!")));
        log.info("DEV saved");
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Best Bites Cafe");
        restaurant.setLocation("123 Main Street, Cityville");
        restaurant.setType(RestaurantType.FASTFOOD);
        restaurant.setAdmin(new User("admin", "admin", LocalDate.of(2000, 04, 04), "admin@gmail.com", "+996777289688", Role.ADMIN, 5L, passwordEncoder.encode("Nurlan21!")));
        restaurant.setServicePro(11L);
        restaurant.getUsers().add(new User("waiter", "waiter", LocalDate.of(2000, 04, 04), "waiter@gmail.com", "+996777289688", Role.WAITER, 1L, passwordEncoder.encode("Nurlan21!")));
        restaurant.getUsers().add(new User("chef", "chef", LocalDate.of(2000, 04, 04), "chef@gmail.com", "+996777289688", Role.CHEF, 5L, passwordEncoder.encode("Nurlan21!")));
        restaurantRepo.save(restaurant);
    }
}
