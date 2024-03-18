package restaurant.service;

import restaurant.dto.response.SimpleResponse;

public interface StopListService {
    SimpleResponse update(Long count, Long menuitemId);

    SimpleResponse updateReason(Long stopListId, String newReason);
}
