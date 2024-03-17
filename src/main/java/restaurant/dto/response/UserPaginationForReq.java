package restaurant.dto.response;

import lombok.*;
import java.time.LocalDate;
@Builder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPaginationForReq {
    private Long id;
    private String email;
    private LocalDate age;
    private Long experience;
    private String phoneNumber;
    private String password;
}
