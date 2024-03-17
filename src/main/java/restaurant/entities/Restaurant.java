package restaurant.entities;

import jakarta.persistence.*;
import lombok.*;
import restaurant.dto.response.RestaurantResponse;
import restaurant.enums.RestaurantType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "restaurants")
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "res_id",allocationSize = 1)
public class Restaurant extends BaseEntity{
    private String name;
    private String location;
    @Enumerated(EnumType.STRING)
    private RestaurantType type;
    private Long servicePro;
    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    private List<User> users = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Menuitem> menuitemList = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.REMOVE})
    private List<RemoveUsersCheques> removeUsersCheques = new ArrayList<>();
    @OneToOne(cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    private User admin;
    @OneToMany
    private List<User> requests = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.REMOVE})
    private List<Category> categories = new ArrayList<>();

    public Restaurant(String name, String location,Long servicePro) {
        this.name = name;
        this.location = location;
        this.servicePro = servicePro;
    }
    public RestaurantResponse convert(){
        return new RestaurantResponse(this.name,this.location,this.type, (long) this.users.size(), (long) menuitemList.size(),this.servicePro,this.admin.getEmail());
    }
}
