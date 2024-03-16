package restaurant.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.SignInReq;
import restaurant.dto.request.UserRequest;
import restaurant.dto.response.SignInResponse;
import restaurant.dto.response.SimpleResponse;
import restaurant.service.UserService;

import java.security.Principal;

@RequestMapping("/api/auth")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthApi {
    private final UserService userService;


    @PostMapping("/saveUser")
    public SimpleResponse signUp(@RequestBody @Valid UserRequest userRequest, Principal principal) {
        log.info("APIIIIIIIIIIIIIIIIII");
        return userService.signUp(principal, userRequest);
    }

    @PutMapping("/signIn")
    private SignInResponse signIn(@RequestBody @Valid SignInReq signInReq) {
        log.info("APIIIIIIIIIIIIIIIIII");
        return userService.signIn(signInReq);
    }
}
