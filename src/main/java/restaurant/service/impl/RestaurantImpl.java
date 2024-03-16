package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.dto.request.RestaurantReq;
import restaurant.dto.request.UpdateRestReq;
import restaurant.dto.response.RestaurantPagination;
import restaurant.dto.response.RestaurantResponse;
import restaurant.dto.response.SimpleResponse;
import restaurant.entities.Restaurant;
import restaurant.entities.User;
import restaurant.enums.RestaurantType;
import restaurant.enums.Role;
import restaurant.exceptions.NotFoundException;
import restaurant.repository.MenuitemRepo;
import restaurant.repository.RestaurantRepo;
import restaurant.repository.UserRepo;
import restaurant.service.RestaurantService;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantImpl implements RestaurantService {
    private final RestaurantRepo restaurantRepo;
    private final MenuitemRepo menuitemRepo;
    private final UserRepo userRepo;
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

    @Override @Transactional
    public SimpleResponse deleteById(Long resId) {
        Restaurant restaurant = restaurantRepo.findById(resId).orElseThrow(() -> new NotFoundException("Not found restaurant with id: " + resId));
        userRepo.deleteAll(restaurant.getUsers());
        menuitemRepo.deleteAll(restaurant.getMenuitemList());
        restaurantRepo.delete(restaurant);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted").build();
    }

    @Override @Transactional
    public SimpleResponse updateById(Long restId, UpdateRestReq updateRestReq, RestaurantType restaurantType) {
        Restaurant restaurant = restaurantRepo.findById(restId).orElseThrow(() -> new NotFoundException("Not found restaurant with id: " + restId));
        if (!Arrays.toString(RestaurantType.values()).contains(restaurantType.name()))
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Restaurant type is invalid select: 'EVROPEISKII,     TURETSKII,     VOSTOCHNYI,     PITSERII,   UZBEKSKII , FASTFOOD'").build();
        restaurant.setName(updateRestReq.name());
        restaurant.setLocation(updateRestReq.location());
        restaurant.setServicePro(updateRestReq.servicePro());
        restaurant.setType(restaurantType);
        return SimpleResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Success updated new name: "  + updateRestReq.name()).build();
    }

    @Override
    public String checkVacancy(Long restId) {
        return restaurantRepo.findById(restId).orElseThrow(() ->new NotFoundException("Not found restaurant with id: " + restId)).getUsers().size() >= 15 ? "Vacancy not is restaurant workers full" : "You can send resume" ;
    }
}
