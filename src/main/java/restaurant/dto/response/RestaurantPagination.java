package restaurant.dto.response;

import lombok.Builder;

import java.util.List;
@Builder
public record RestaurantPagination(int page, int size, List<RestaurantResponse> restaurantResponses) {
}
