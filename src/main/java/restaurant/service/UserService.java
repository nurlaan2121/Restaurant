package restaurant.service;
import restaurant.dto.request.SignInReq;
import restaurant.dto.request.UserReqForRest;
import restaurant.dto.request.UserRequest;
import restaurant.dto.response.SignInResponse;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.UserPagination;
import restaurant.dto.response.UserResponse;
import restaurant.enums.Role;

import java.security.Principal;

public interface UserService {
    SimpleResponse signUp(Principal principal,UserRequest userRequest);

    SignInResponse signIn(SignInReq signInReq);

    UserResponse findById(Long userId);

    SimpleResponse deleteById(Long userId);

    SimpleResponse updateUserById(Long upUsId,UserRequest newUser);

    UserPagination getAll(int page, int size);

    SimpleResponse reqForRest(Long restId, UserReqForRest userReq, Role role);
}
