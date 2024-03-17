package restaurant.dto.response.category;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryPagination(int page, int size, List<CategoryRes> categoryRes) {
}
