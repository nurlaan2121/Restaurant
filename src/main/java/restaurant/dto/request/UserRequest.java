package restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import restaurant.entities.User;
import restaurant.enums.Role;
import restaurant.validation.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "first name is blank")
    private String firstName;
    @NotBlank(message = "last name is blank")
    private String lastName;
    @DateOBValidation
    private LocalDate dateOfBirth;
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;
    @PhoneNumberValidation
    private String phoneNumber;
    private Role role;
    @NotNegative
    private Long experience;

    public User build() {
        return new User(this.firstName, this.lastName, this.dateOfBirth, this.email, this.phoneNumber, this.role, this.experience,this.password);
    }
}
