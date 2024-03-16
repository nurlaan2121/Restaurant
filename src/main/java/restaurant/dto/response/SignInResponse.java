package restaurant.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;
@Builder
public record SignInResponse(HttpStatus httpStatus,String message,String token) {
}
