package restaurant.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserPagination(int page, int size, List<UserResForPagination> userRes) {
}
