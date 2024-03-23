package restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record StopListReq(@NotBlank String name) {
}
