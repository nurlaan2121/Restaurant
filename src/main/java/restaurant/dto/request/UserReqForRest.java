package restaurant.dto.request;

import lombok.*;
import restaurant.entities.User;
import restaurant.enums.Role;
import restaurant.validation.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReqForRest {
    @EmailValidation
    private String email;
    @DateOBValidation
    private LocalDate age;
    @NotNegative
    private Long experience;
    @PhoneNumberValidation
    private String phoneNumber;
    @PasswordValidation
    private String password;
    public User build(Role role){
        return new User(this.email,this.age,this.experience,this.phoneNumber,this.password,role);
    }
}
