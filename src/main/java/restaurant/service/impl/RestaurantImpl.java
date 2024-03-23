package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.dto.request.RestaurantReq;
import restaurant.dto.request.UpdateRestReq;
import restaurant.dto.request.UserReqForRest;
import restaurant.dto.response.*;
import restaurant.entities.*;
import restaurant.enums.ActionForReq;
import restaurant.enums.RestaurantType;
import restaurant.enums.Role;
import restaurant.exceptions.NotFoundException;
import restaurant.repository.*;
import restaurant.service.RestaurantService;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantImpl implements RestaurantService {
    private final RestaurantRepo restaurantRepo;
    private final MenuitemRepo menuitemRepo;
    private final UserRepo userRepo;
    private final ChequeRepo chequeRepo;
    private final StopListRepo stopListRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SimpleResponse save(RestaurantReq restaurantReq, RestaurantType restaurantType) {
        if (!userRepo.existByEmail(restaurantReq.getEmail()))
            return SimpleResponse.builder().httpStatus(HttpStatus.CONFLICT).message("This email already exists:  " + restaurantReq.getEmail()).build();
        if (!Arrays.toString(RestaurantType.values()).contains(restaurantType.name()))
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Restaurant type is blank select: 'EVROPEISKII,     TURETSKII,     VOSTOCHNYI,     PITSERII,   UZBEKSKII , FASTFOOD'").build();
        User admin = new User(restaurantReq.getEmail(), passwordEncoder.encode(restaurantReq.getPassword()), Role.ADMIN);
        userRepo.save(admin);
        Restaurant restaurant = restaurantReq.build();
        restaurant.setType(restaurantType);
        restaurantRepo.save(restaurant);
        restaurant.setAdmin(admin);
        restaurant.getUsers().add(admin);
        return SimpleResponse.builder().
                httpStatus(HttpStatus.ACCEPTED).
                message("Restaurant with name: " + restaurantReq.getName() + "  success saved").
                build();
    }

    @Override
    public RestaurantPagination getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Restaurant> restaurantPage = restaurantRepo.findAll(pageable);
        List<Restaurant> content = restaurantPage.getContent();
        List<RestaurantResponse> restaurantResponses = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            restaurantResponses.add(content.get(i).convert());
        }
        return RestaurantPagination.builder().page(restaurantPage.getNumber() + 1).size(restaurantPage.getTotalPages()).
                restaurantResponses(restaurantResponses).
                build();
    }

    @Override
    public RestaurantResponse findById(Long restId) {
        return restaurantRepo.findById(restId).orElseThrow(() -> new NotFoundException("Not found restaurant with id: " + restId)).convert();
    }

    @Override
    public RestaurantResponse findById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.error(authentication.getName());
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        if (restWithAdmin == null) throw new RuntimeException("YOUR TOKEN INVALID");
        return restWithAdmin.convert();

    }


    @Override
    @Transactional
    public SimpleResponse updateById(UpdateRestReq updateRestReq, RestaurantType restaurantType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restaurant = restaurantRepo.getRestWithAdmin(authentication.getName());
        if (restaurant == null) return SimpleResponse.builder().
                httpStatus(HttpStatus.BAD_REQUEST).message("YOUR TOKEN INVALID").build();
        if (!Arrays.toString(RestaurantType.values()).contains(restaurantType.name()))
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Restaurant type is invalid select: 'EVROPEISKII,     TURETSKII,     VOSTOCHNYI,     PITSERII,   UZBEKSKII , FASTFOOD'").build();
        restaurant.setName(updateRestReq.name());
        restaurant.setLocation(updateRestReq.location());
        restaurant.setServicePro(updateRestReq.servicePro());
        restaurant.setType(restaurantType);
        return SimpleResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Success updated new name: " + updateRestReq.name()).build();
    }

    @Override
    @Transactional
    public SimpleResponse updateById(Long restId, UpdateRestReq updateRestReq, RestaurantType restaurantType) {
        Restaurant restaurant = restaurantRepo.findById(restId).orElseThrow(() -> new NotFoundException("Restaurant with id: " + restId + "not found!"));
        if (!Arrays.toString(RestaurantType.values()).contains(restaurantType.name()))
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Restaurant type is invalid select: 'EVROPEISKII,     TURETSKII,     VOSTOCHNYI,     PITSERII,   UZBEKSKII , FASTFOOD'").build();
        restaurant.setName(updateRestReq.name());
        restaurant.setLocation(updateRestReq.location());
        restaurant.setServicePro(updateRestReq.servicePro());
        restaurant.setType(restaurantType);
        return SimpleResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Success updated new name: " + updateRestReq.name()).build();
    }

    @Override
    public String checkVacancy(Long restId) {
        return restaurantRepo.findById(restId).orElseThrow(() -> new NotFoundException("Not found restaurant with id: " + restId)).getUsers().size() >= 15 ? "Vacancy not is restaurant workers full" : "You can send resume";
    }

    @Override
    @Transactional
    public SimpleResponse delete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restaurant = restaurantRepo.getRestWithAdmin(authentication.getName());
        if (restaurant == null)
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("YOUR TOKEN INVALID").build();
        for (int i = 0; i < restaurant.getUsers().size(); i++) {
            List<Cheque> cheques = restaurant.getUsers().get(i).getCheques();
            restaurant.getUsers().get(i).getCheques().clear();
            log.info(String.valueOf(cheques.size()));
            chequeRepo.deleteAll(cheques);
        }
        userRepo.deleteAll(restaurant.getUsers());
        List<Menuitem> menuitemList = restaurant.getMenuitemList();
        for (int i = 0; i < menuitemList.size(); i++) {
            Long id = menuitemList.get(i).getId();
            StopList stopList = stopListRepo.get(menuitemList.get(i).getId());
            if (stopList != null) {
                stopListRepo.delete(stopList);
            }
            Cheque cheque = chequeRepo.getByMen(id);
            if (cheque != null) {
                chequeRepo.delete(cheque);
            }
        }
        menuitemRepo.deleteAll(restaurant.getMenuitemList());
        restaurantRepo.delete(restaurant);

        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted").build();
    }

    @Override
    public PaginationUserReq getAllReq(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userRepo.findAll(pageable);
        List<User> content = userPage.getContent();
        List<UserPaginationForReq> userResFPag = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
            if (restWithAdmin.getRequests().contains(content.get(i))) {
                log.info("ADDED to PAGINATION! " + "count:  " + i);
                userResFPag.add(content.get(i).convertForReq());
            }
        }
        return PaginationUserReq.builder().page(userPage.getNumber() + 1)
                .size(userPage.getTotalPages()).userRes(userResFPag).
                build();
    }

    @Override
    @Transactional
    public SimpleResponse reqAccOrRem(Long reqId, ActionForReq action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        for (User request : restWithAdmin.getRequests()) {
            if (request.getId().equals(reqId)) {
                if (action.equals(ActionForReq.ACCEPT)) {
                    boolean add = restWithAdmin.getUsers().add(request);
                    log.info(String.valueOf(add));
                    restWithAdmin.getRequests().remove(request);
                    log.info(request.getId() + "Success accepted");
                    return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success accepted request").build();
                } else {
                    restWithAdmin.getRequests().remove(request);
                    userRepo.deleteById(reqId);
                    log.info(request.getId() + "Success remove in date base");
                    return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success removed request").build();
                }
            }
        }
        throw new NotFoundException("This request :" + reqId + " not found!");
    }

    @Override
    @Transactional
    public SimpleResponse deleteById(Long resId) {
        Restaurant restaurant = restaurantRepo.findById(resId).orElseThrow(() -> new NotFoundException("Not found restaurant with id: " + resId));
        if (restaurant == null)
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("YOUR TOKEN INVALID").build();
        for (int i = 0; i < restaurant.getUsers().size(); i++) {
            List<Cheque> cheques = restaurant.getUsers().get(i).getCheques();
            restaurant.getUsers().get(i).getCheques().clear();
            log.info(String.valueOf(cheques.size()));
            chequeRepo.deleteAll(cheques);
        }
        userRepo.deleteAll(restaurant.getUsers());
        List<Menuitem> menuitemList = restaurant.getMenuitemList();
        for (int i = 0; i < menuitemList.size(); i++) {
            Long id = menuitemList.get(i).getId();
            StopList stopList = stopListRepo.get(menuitemList.get(i).getId());
            if (stopList != null) {
                stopListRepo.delete(stopList);
            }
            Cheque cheque = chequeRepo.getByMen(id);
            if (cheque != null) {
                chequeRepo.delete(cheque);
            }
        }
        menuitemRepo.deleteAll(restaurant.getMenuitemList());
        restaurantRepo.delete(restaurant);

        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted").build();
    }
}
