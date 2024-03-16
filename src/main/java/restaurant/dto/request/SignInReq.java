package restaurant.dto.request;

import lombok.Builder;
import restaurant.validation.EmailValidation;
import restaurant.validation.PasswordValidation;
@Builder
public record SignInReq(@EmailValidation String email,@PasswordValidation String password) {
}
