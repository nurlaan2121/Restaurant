package restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryReq(@NotBlank String name) {

}
