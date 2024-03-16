package restaurant.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurant.entities.User;
import restaurant.enums.Role;
import restaurant.repository.UserRepo;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InitialService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    @PostConstruct
    private void saveDev(){
        userRepo.save(new User("dev","devof", LocalDate.of(2000,04,04),"dev@gmail.com","+996777289688", Role.DEV,15L,passwordEncoder.encode("Dev12#$")));
    }
}
