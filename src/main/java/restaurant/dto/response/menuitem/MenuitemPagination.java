package restaurant.dto.response.menuitem;

import lombok.Builder;

import java.util.List;
@Builder
public record MenuitemPagination(int page, int size, List<MenuitemRes> menuitemRes) {

}
