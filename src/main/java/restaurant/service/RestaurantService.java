package restaurant.service;

import restaurant.dto.request.RestaurantReq;
import restaurant.dto.request.UpdateRestReq;
import restaurant.dto.response.*;
import restaurant.enums.ActionForReq;
import restaurant.enums.RestaurantType;

public interface RestaurantService {
    SimpleResponse save(RestaurantReq restaurantReq, RestaurantType restaurantType);

    RestaurantPagination getAll(int page, int size);

    RestaurantResponse findById(Long restId);
    RestaurantResponse findById();

    SimpleResponse deleteById(Long resId);

    SimpleResponse updateById(UpdateRestReq updateRestReq, RestaurantType restaurantType);
    SimpleResponse updateById(Long restId,UpdateRestReq updateRestReq, RestaurantType restaurantType);

    String checkVacancy(Long restId);

    SimpleResponse delete();

    PaginationUserReq getAllReq(int page, int size);

    SimpleResponse reqAccOrRem(Long reqId, ActionForReq action);
}
