package restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import restaurant.validation.NotNegative;
import restaurant.validation.ServiceValidation;

public record UpdateRestReq(@NotBlank String name,@NotBlank String location,@NotNegative Long servicePro) {
}
