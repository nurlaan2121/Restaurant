package restaurant.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restaurant.service.UserService;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class CategoryApi {
    private final UserService userService;
}
