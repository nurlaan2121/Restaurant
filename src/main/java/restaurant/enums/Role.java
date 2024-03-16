package restaurant.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    DEV,
    ADMIN,
    WAITER,
    CHEF;
    @Override
    public String getAuthority() {
        return name();
    }
}
