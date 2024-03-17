package restaurant.dto.response;
import lombok.*;
import restaurant.enums.Role;
import java.time.LocalDate;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResForPagination {
    private String firsName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    private Role role;
    private Long experience;
    private Long checkCount;
}
