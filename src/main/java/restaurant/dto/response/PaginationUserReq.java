package restaurant.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginationUserReq(int page, int size, List<UserPaginationForReq> userRes) {
}
