package restaurant.service.impl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.dto.request.SignInReq;
import restaurant.dto.request.UserRequest;
import restaurant.dto.response.SignInResponse;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.UserResponse;
import restaurant.entities.Cheque;
import restaurant.entities.Restaurant;
import restaurant.entities.User;
import restaurant.enums.Role;
import restaurant.exceptions.NotFoundException;
import restaurant.repository.ChequeRepo;
import restaurant.repository.RestaurantRepo;
import restaurant.repository.UserRepo;
import restaurant.security.jwt.JwtService;
import restaurant.service.UserService;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ChequeRepo chequeRepo;
    private final RestaurantRepo restaurantRepo;

    public User myFindById(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Wish user not found: " + userId));
    }

    @Override @Transactional
    public SimpleResponse signUp(Principal principal, UserRequest userRequest) {
        Restaurant restaurant = restaurantRepo.getRestWithAdmin(principal.getName());
        if (!userRepo.existByEmail(userRequest.getEmail()))
            return SimpleResponse.builder().httpStatus(HttpStatus.CONFLICT).
                    message("Email already exists: " + userRequest.getEmail() + " !").build();
        if (userRequest.getRole().equals(Role.CHEF)) {
            if (LocalDate.now().getYear() - userRequest.getDateOfBirth().getYear() > 24 && LocalDate.now().getYear() - userRequest.getDateOfBirth().getYear() < 46) {
                userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                User chef = userRequest.build();
                userRepo.save(chef);
               restaurant.getUsers().add(chef);
                return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success saved").
                        build();
            } else {
                return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Chef's age must be between 25 and 45 years.").build();
            }
        } else if (userRequest.getRole().equals(Role.WAITER)) {
            if (LocalDate.now().getYear() - userRequest.getDateOfBirth().getYear() > 17 && LocalDate.now().getYear() - userRequest.getDateOfBirth().getYear() < 31) {
                userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                User waiter = userRequest.build();
                userRepo.save(waiter);
                restaurant.getUsers().add(waiter);
                return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success saved").
                        build();
            } else {
                return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Waiter's age must be between 18 and 30 years.").build();
            }
        }
        return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Role invalid").build();
    }

    @Override
    public SignInResponse signIn(SignInReq signInReq) {
        log.info("SERIVCEEEEEEEEEE");
        User user = userRepo.findByEmail(signInReq.email()).orElse(null);
        if (user == null) return SignInResponse.builder().
                httpStatus(HttpStatus.NOT_FOUND).message("Email invalid!").
                build();
        else {
            if (passwordEncoder.matches(signInReq.password(), user.getPassword())) {
                return SignInResponse.builder().
                        httpStatus(HttpStatus.OK).token(jwtService.createTokenForStud(user)).message("ROLE: " + user.getRole()).build();
            } else {
                return SignInResponse.builder().
                        httpStatus(HttpStatus.BAD_REQUEST).message("Password invalid!").build();
            }
        }
    }

    @Override
    public UserResponse findById(Long userId) {
        UserResponse userResponse = myFindById(userId).convert();
        Map<Long, BigDecimal> returnMap = new HashMap<>();
        List<Cheque> chequeList = chequeRepo.findAllByUserId(userId);
        BigDecimal reduce = chequeList.stream()
                .map(Cheque::getPriceAverage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        returnMap.put((long) chequeList.size(), reduce);
        userResponse.setCheckWithCountAndSum(returnMap);
        return userResponse;
    }
}
