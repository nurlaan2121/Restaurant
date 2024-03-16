package restaurant.service;

import restaurant.dto.request.RestaurantReq;
import restaurant.dto.request.UpdateRestReq;
import restaurant.dto.response.RestaurantPagination;
import restaurant.dto.response.RestaurantResponse;
import restaurant.dto.response.SimpleResponse;
import restaurant.enums.RestaurantType;

public interface RestaurantService {
    SimpleResponse save(RestaurantReq restaurantReq, RestaurantType restaurantType);

    RestaurantPagination getAll(int page, int size);

    RestaurantResponse findById(Long restId);

    SimpleResponse deleteById(Long resId);

    SimpleResponse updateById(Long restId, UpdateRestReq updateRestReq, RestaurantType restaurantType);

    String checkVacancy(Long restId);
}
