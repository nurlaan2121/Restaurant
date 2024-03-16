package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restaurant.repository.MenuitemRepo;
import restaurant.service.MenuitemService;
@Service
@RequiredArgsConstructor
public class MenuitemImpl implements MenuitemService {
    private final MenuitemRepo menuitemRepo;
}
