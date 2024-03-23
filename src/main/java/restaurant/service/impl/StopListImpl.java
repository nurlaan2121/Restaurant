package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.dto.response.SimpleResponse;
import restaurant.entities.Menuitem;
import restaurant.entities.Restaurant;
import restaurant.entities.StopList;
import restaurant.entities.User;
import restaurant.enums.Role;
import restaurant.exceptions.ForbiddenException;
import restaurant.exceptions.NotFoundException;
import restaurant.repository.MenuitemRepo;
import restaurant.repository.RestaurantRepo;
import restaurant.repository.StopListRepo;
import restaurant.repository.UserRepo;
import restaurant.service.StopListService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StopListImpl implements StopListService {
    private final StopListRepo stopListRepo;
    private final RestaurantRepo restaurantRepo;
    private final MenuitemRepo menuitemRepo;
    private final UserRepo userRepo;

    @Override @Transactional
    public SimpleResponse update(Long count, Long menuitemId) {
        if (count<=0) return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Write correct count!").build();
        String adminOrChef = SecurityContextHolder.getContext().getAuthentication().getName();
        User adOrChe = userRepo.findByEmail(adminOrChef).orElseThrow(() -> new RuntimeException("Your token invalid!"));
        if (adOrChe.getRole().equals(Role.CHEF)){
            Menuitem menuitem = menuitemRepo.findById(menuitemId).orElseThrow(() -> new NotFoundException("Menuitem with id not found!  " + menuitemId));
            Restaurant restWithChef = restaurantRepo.getRestWithChef(adOrChe.getId());
            if (!restWithChef.getMenuitemList().contains(menuitem)) throw new ForbiddenException("Forbidden 403 you no can updated this stop list!" + menuitemId);
            menuitem.setCount(count);
            StopList stopList = stopListRepo.getByIdMenuitemId(menuitemId);
            stopListRepo.delete(stopList);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success updated with : " + count).build();
        }
        Menuitem menuitem = menuitemRepo.findById(menuitemId).orElseThrow(() -> new NotFoundException("Menuitem with id not found!  " + menuitemId));
        Restaurant restWithChef = restaurantRepo.getRestWithAdmin(adminOrChef);
        if (!restWithChef.getMenuitemList().contains(menuitem)) throw new ForbiddenException("Forbidden 403 you no can updated this stop list!" + menuitemId);
        menuitem.setCount(count);
        StopList stopList = stopListRepo.getByIdMenuitemId(menuitemId);
        stopListRepo.delete(stopList);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success updated with : " + count).build();
    }

    @Override @Transactional
    public SimpleResponse updateReason(Long stopListId, String newReason) {
        if (newReason.length()<5) return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Write correct reason").build();
        String emailAdmin = SecurityContextHolder.getContext().getAuthentication().getName();
        StopList stopList = stopListRepo.getByIdStopListIdId(stopListId,emailAdmin);
        if (stopList == null) throw new NotFoundException("This stop list not found : " + stopListId);
        stopList.setReason(newReason);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success updated!").build();
    }
}
