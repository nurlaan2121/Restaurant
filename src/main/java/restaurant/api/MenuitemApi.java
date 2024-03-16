package restaurant.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restaurant.service.MenuitemService;

@RequiredArgsConstructor
@RequestMapping
@RestController

public class MenuitemApi {
    private final MenuitemService menuitemService;
}
