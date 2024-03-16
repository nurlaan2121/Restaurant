package restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import restaurant.validation.ServiceValidation;

public record UpdateRestReq(String name, String location, Long servicePro) {
}
