package restaurant.service;
import restaurant.dto.request.SignInReq;
import restaurant.dto.request.UserRequest;
import restaurant.dto.response.SignInResponse;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.UserResponse;

import java.security.Principal;

public interface UserService {
    SimpleResponse signUp(Principal principal,UserRequest userRequest);

    SignInResponse signIn(SignInReq signInReq);

    UserResponse findById(Long userId);
}
