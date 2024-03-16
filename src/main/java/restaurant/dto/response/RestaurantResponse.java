package restaurant.dto.response;

import jakarta.persistence.*;
import lombok.*;
import restaurant.entities.Menuitem;
import restaurant.entities.User;
import restaurant.enums.RestaurantType;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
    private String name;
    private String location;
    private RestaurantType type;
    private Long countEmployees;
    private Long countMenuitem;
    private Long servicePro;
    private String admin;
}
