package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restaurant.repository.StopListRepo;
import restaurant.service.StopListService;
@Service
@RequiredArgsConstructor
public class StopListImpl implements StopListService {
    private final StopListRepo stopListRepo;
}
