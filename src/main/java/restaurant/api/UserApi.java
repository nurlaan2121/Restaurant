package restaurant.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restaurant.dto.response.UserResponse;
import restaurant.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserApi {
    private final UserService userService;
    @GetMapping("/findById/{userId}")
    public UserResponse findById(@PathVariable Long userId){
        return userService.findById(userId);
    }
}
