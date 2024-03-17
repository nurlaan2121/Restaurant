package restaurant.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import restaurant.dto.response.UserPaginationForReq;
import restaurant.dto.response.UserResForPagination;
import restaurant.dto.response.UserResponse;
import restaurant.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "userId_gen", allocationSize = 1)
public class User extends BaseEntity implements UserDetails {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Long experience;
    @OneToMany()
    private List<Cheque> cheques = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getName() {
        return this.firstName + this.lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(String firstName, String lastName, LocalDate dateOfBirth, String email, String phoneNumber, Role role, Long experience, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.experience = experience;
        this.password = password;
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserResponse convert() {
        return new UserResponse(this.firstName, this.lastName, this.dateOfBirth, this.email, this.phoneNumber, this.role, this.experience);
    }

    public UserResForPagination convertForRes() {
        return new UserResForPagination(this.firstName, this.lastName, this.dateOfBirth, this.email, this.phoneNumber, this.role, this.experience, (long) cheques.size());
    }public UserPaginationForReq convertForReq() {
        return new UserPaginationForReq(super.getId(),this.email,this.dateOfBirth,this.experience,this.phoneNumber,this.password);
    }

    public User(String email, LocalDate dateOfBirth, Long experience, String phoneNumber, String password, Role role) {
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.experience = experience;
    }

}
