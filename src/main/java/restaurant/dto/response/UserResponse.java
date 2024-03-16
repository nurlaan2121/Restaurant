package restaurant.dto.response;

import lombok.*;
import restaurant.enums.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String firsName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
   private Role role;
   private Long experience;
   private Map<Long, BigDecimal> checkWithCountAndSum = new HashMap<>();


    public UserResponse(String firsName, String lastName, LocalDate dateOfBirth, String email, String phoneNumber, Role role, Long experience) {
        this.firsName = firsName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.experience = experience;
    }
}