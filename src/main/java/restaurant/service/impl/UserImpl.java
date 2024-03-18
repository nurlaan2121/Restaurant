package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.dto.request.SignInReq;
import restaurant.dto.request.UserReqForRest;
import restaurant.dto.request.UserRequest;
import restaurant.dto.response.*;
import restaurant.entities.Cheque;
import restaurant.entities.RemoveUsersCheques;
import restaurant.entities.Restaurant;
import restaurant.entities.User;
import restaurant.enums.Role;
import restaurant.exceptions.ForbiddenException;
import restaurant.exceptions.NotFoundException;
import restaurant.repository.ChequeRepo;
import restaurant.repository.RemoveChequeRepo;
import restaurant.repository.RestaurantRepo;
import restaurant.repository.UserRepo;
import restaurant.security.jwt.JwtService;
import restaurant.service.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ChequeRepo chequeRepo;
    private final RemoveChequeRepo removeChequeRepo;
    private final RestaurantRepo restaurantRepo;
    public User myFindById(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Wish user not found: " + userId));
    }

    @Override
    @Transactional
    public SimpleResponse signUp(Principal principal, UserRequest userRequest) {
        Restaurant restaurant = restaurantRepo.getRestWithAdmin(principal.getName());
        log.error(principal.getName());
        if (restaurant == null)
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("YOUR TOKEN INVALID").build();
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (userRepo.findByEmail(email).get().getRole().equals(Role.DEV)) {
            User user = myFindById(userId);
            UserResponse userResponse = user.convert();
            Map<Long, BigDecimal> returnMap = new HashMap<>();
            List<Cheque> chequeList = chequeRepo.findAllByUserId(userId);
            BigDecimal reduce = chequeList.stream()
                    .map(Cheque::getPriceAverage)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            returnMap.put((long) chequeList.size(), reduce);
            userResponse.setCheckWithCountAndSum(returnMap);
            return userResponse;

        } else if (userRepo.findByEmail(email).get().getRole().equals(Role.ADMIN)) {
            Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(email);
            User user = myFindById(userId);
            if (!restWithAdmin.getUsers().contains(user))
                throw new NotFoundException("This user id not found: " + userId);
            UserResponse userResponse = user.convert();
            Map<Long, BigDecimal> returnMap = new HashMap<>();
            List<Cheque> chequeList = chequeRepo.findAllByUserId(userId);
            BigDecimal reduce = chequeList.stream()
                    .map(Cheque::getPriceAverage)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            returnMap.put((long) chequeList.size(), reduce);
            userResponse.setCheckWithCountAndSum(returnMap);
            return userResponse;
        } else throw new ForbiddenException("Forbidden 403 your no can search users!");
    }

    @Override
    @Transactional
    public SimpleResponse deleteById(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User adminOrDev = userRepo.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("YOUR TOKEN INVALID"));
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(adminOrDev.getEmail());
        if (adminOrDev.getRole().equals(Role.ADMIN)) {
            User searchUser = myFindById(userId);
            if (!restWithAdmin.getUsers().contains(searchUser))
                return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("You no can deleted this user : " + userId).build();
            List<Cheque> searchUserCheques = searchUser.getCheques();
            for (int i = 0; i < searchUserCheques.size(); i++) {
                String foods = searchUserCheques.get(i).getMenuitemList().toString();
                removeChequeRepo.save(new RemoveUsersCheques(searchUser.getEmail(), searchUserCheques.get(i).getPriceAverage(), searchUserCheques.get(i).getCreatedAdCheque(), foods));
            }
            chequeRepo.deleteAll(searchUserCheques);
            restWithAdmin.getUsers().remove(searchUser);
            userRepo.delete(searchUser);
            return SimpleResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Success deleted user: " + searchUser.getName()).build();
        }
        //for dev using
        User searchUser = myFindById(userId);
        List<Cheque> searchUserCheques = searchUser.getCheques();
        for (int i = 0; i < searchUserCheques.size(); i++) {
            String foods = searchUserCheques.get(i).getMenuitemList().toString();
            removeChequeRepo.save(new RemoveUsersCheques(searchUser.getEmail(), searchUserCheques.get(i).getPriceAverage(), searchUserCheques.get(i).getCreatedAdCheque(), foods));
        }
        chequeRepo.deleteAll(searchUserCheques);
        restWithAdmin.getUsers().remove(searchUser);
        userRepo.delete(searchUser);
        return SimpleResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Success deleted user: " + searchUser.getName()).build();
    }

    @Override
    @Transactional
    public SimpleResponse updateUserById(Long upUsId, UserRequest userRequest) {
        if (userRequest.getRole().equals(Role.ADMIN) || userRequest.getRole().equals(Role.DEV))
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Invalid role!").build();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User adminOrDev = userRepo.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("Your token invalid"));
        if (adminOrDev.getRole().equals(Role.ADMIN)) {
            User findUser = myFindById(upUsId);
            Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
            if (!restWithAdmin.getUsers().contains(findUser))
                throw new NotFoundException("You no can update this user!");
            myUpdate(findUser, userRequest);
            User user = userRepo.findById(upUsId).get();
            String token = jwtService.createTokenForStud(user);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success update YOUR TOKEN: " + token).build();
        }
        User findUser = myFindById(upUsId);
        myUpdate(findUser, userRequest);
        User user = userRepo.findById(upUsId).get();
        String token = jwtService.createTokenForStud(user);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success update YOUR TOKEN: " + token).build();
    }

    @Override
    public UserPagination getAll(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userRepo.findAll(pageable);
        List<User> content = userPage.getContent();
        List<UserResForPagination> userResFPag = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
            if (restWithAdmin.getUsers().contains(content.get(i))) {
                if (!content.get(i).getRole().equals(Role.ADMIN)) {
                    log.error("ADMINDI SALBAI ATAT");
                    userResFPag.add(content.get(i).convertForRes());
                }
            }
        }
        return UserPagination.builder().page(userPage.getNumber() + 1)
                .size(userPage.getTotalPages()).userRes(userResFPag).
                build();
    }

    @Override
    @Transactional
    public SimpleResponse reqForRest(Long restId, UserReqForRest userReq, Role role) {
        if (!userRepo.existByEmail(userReq.getEmail())) return SimpleResponse.builder().httpStatus(HttpStatus.CONFLICT).
                message("Email already exists: " + userReq.getEmail() + " !").build();
        Restaurant restaurant = restaurantRepo.findById(restId).orElseThrow(() -> new NotFoundException("This restaurant not found: " + restId));
        if (role.equals(Role.DEV) || role.equals(Role.ADMIN))
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Role invalid").build();
        if (role.equals(Role.CHEF)) {
            if (LocalDate.now().getYear() - userReq.getAge().getYear() > 24 && LocalDate.now().getYear() - userReq.getAge().getYear() < 46) {
                userReq.setPassword(passwordEncoder.encode(userReq.getPassword()));
                User chef = userReq.build(role);
                userRepo.save(chef);
                restaurant.getRequests().add(chef);
                return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success saved").
                        build();
            } else {
                return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Chef's age must be between 25 and 45 years.").build();
            }
        } else {
            if (LocalDate.now().getYear() - userReq.getAge().getYear() > 17 && LocalDate.now().getYear() - userReq.getAge().getYear() < 31) {
                userReq.setPassword(passwordEncoder.encode(userReq.getPassword()));
                User waiter = userReq.build(role);
                userRepo.save(waiter);
                restaurant.getRequests().add(waiter);
                return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success saved").
                        build();
            } else {
                return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Waiter's age must be between 18 and 30 years.").build();
            }
        }
    }
    private void myUpdate(User findUser, UserRequest userRequest) {
        findUser.setEmail(userRequest.getEmail());
        findUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        findUser.setExperience(userRequest.getExperience());
        findUser.setRole(userRequest.getRole());
        findUser.setFirstName(userRequest.getFirstName());
        findUser.setLastName(userRequest.getLastName());
        findUser.setDateOfBirth(userRequest.getDateOfBirth());
        findUser.setPhoneNumber(userRequest.getPhoneNumber());
    }
}
