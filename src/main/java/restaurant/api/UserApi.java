package restaurant.api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.UserReqForRest;
import restaurant.dto.request.UserRequest;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.UserPagination;
import restaurant.dto.response.UserResponse;
import restaurant.enums.Role;
import restaurant.service.RestaurantService;
import restaurant.service.UserService;
import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
@Slf4j
public class UserApi {
    private final UserService userService;
    private final RestaurantService restaurantService;
    @Secured({"ADMIN","DEV"})
    @GetMapping("/findById/{userId}")
    public UserResponse findById(@PathVariable Long userId){
        return userService.findById(userId);
    }
    @Secured("ADMIN")
    @PostMapping("/saveUser")
    public SimpleResponse saveUserInMyRes(@RequestBody @Valid UserRequest userRequest, Principal principal) {
        log.info("APIIIIIIIIIIIIIIIIII");
        return userService.signUp(principal, userRequest);
    }
    @Secured({"ADMIN","DEV"})
    @DeleteMapping("/deleteById/{userId}")
    public SimpleResponse deleteById(@PathVariable Long userId){
        return userService.deleteById(userId);
    }
    @Secured({"ADMIN","DEV"})
    @PutMapping("/updateById/{upUsId}")
    public SimpleResponse updateById(@PathVariable Long upUsId,@RequestBody @Valid UserRequest userRequest){
        return userService.updateUserById(upUsId,userRequest);
    }
    @Secured({"ADMIN"})
    @GetMapping("/getAll")
    public UserPagination getAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return userService.getAll(page,size);
    }
    @PostMapping("/requestForRest/{restId}")
    public SimpleResponse requestForRest(@PathVariable Long restId, @RequestBody @Valid UserReqForRest userReq, @RequestParam Role role){
        return userService.reqForRest(restId,userReq,role);
    }
    @GetMapping("/checkVacancy/{restId}")
    public String checkVacancy(@PathVariable Long restId){
        return restaurantService.checkVacancy(restId);
    }


}
