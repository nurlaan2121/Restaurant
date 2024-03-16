package restaurant.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import restaurant.entities.Restaurant;
import restaurant.enums.RestaurantType;
import restaurant.validation.EmailValidation;
import restaurant.validation.NotNegative;
import restaurant.validation.PasswordValidation;
import restaurant.validation.ServiceValidation;
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantReq {
    @NotBlank(message = "name is blank")
    private String name;
    @NotBlank(message = "location is blank")
    private String location;
    @ServiceValidation
    private Long servicePro;
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;
    public Restaurant build(){
        return new Restaurant(this.name,this.location,this.servicePro);
    }
}
