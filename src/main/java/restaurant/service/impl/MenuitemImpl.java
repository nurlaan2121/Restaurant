package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.dto.request.MenuitemReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.UserResForPagination;
import restaurant.dto.response.menuitem.MenuitemPagination;
import restaurant.dto.response.menuitem.MenuitemRes;
import restaurant.entities.*;
import restaurant.enums.Role;
import restaurant.exceptions.ForbiddenException;
import restaurant.exceptions.NotFoundException;
import restaurant.repository.*;
import restaurant.service.MenuitemService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuitemImpl implements MenuitemService {
    private final MenuitemRepo menuitemRepo;
    private final UserRepo userRepo;
    private final RestaurantRepo restaurantRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final CategoryRepo categoryRepo;
    private final StopListRepo stopListRepo;
    private final ChequeRepo chequeRepo;

    private SubCategory mySubCatFindById(Long supCatId) {
        return subCategoryRepo.findById(supCatId).orElseThrow(() -> new NotFoundException("With Sub Category not found" + supCatId));
    }

    private Menuitem myFindById(Long menuitemId) {
        return menuitemRepo.findById(menuitemId).orElseThrow(() -> new NotFoundException("With menuitem not found" + menuitemId));
    }


    @Override
    @Transactional
    public SimpleResponse save(MenuitemReq menuitemReq) {
        String currentAdminEmailorChef = SecurityContextHolder.getContext().getAuthentication().getName();
        SubCategory subCategory = mySubCatFindById(menuitemReq.getSubCategory());
        User user = userRepo.findByEmail(currentAdminEmailorChef).orElseThrow(() -> new NotFoundException("This email noty found!"));
        if (user.getRole().equals(Role.DEV) || user.getRole().equals(Role.WAITER)) {
            return SimpleResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("You no can save menuitem!").build();
        }
        if (user.getRole().equals(Role.CHEF)) {
            Restaurant restWithChef = restaurantRepo.getRestWithChef(user.getId());
            Menuitem exFind = menuitemRepo.findByName(menuitemReq.getName(),restWithChef.getId());
            if (exFind != null)
                return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("This name already exists!").build();
            Category category = categoryRepo.getCatByResId(restWithChef.getId(), subCategory.getId());
            if (!category.getSubCategories().contains(subCategory)) return SimpleResponse.builder().
                    httpStatus(HttpStatus.FORBIDDEN).message("Forbidden 403").build();
            Menuitem menuitem = menuitemReq.convert();
            menuitemRepo.save(menuitem);
            subCategory.getMenuitemList().add(menuitem);
            restWithChef.getMenuitemList().add(menuitem);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success saved").build();

        }
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(currentAdminEmailorChef);
        Menuitem exFind = menuitemRepo.findByName(menuitemReq.getName(),restWithAdmin.getId());
        if (exFind != null)
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("This name already exists!").build();
        Category category = categoryRepo.getCatByResId(restWithAdmin.getId(), subCategory.getId());
        if (!category.getSubCategories().contains(subCategory)) return SimpleResponse.builder().
                httpStatus(HttpStatus.FORBIDDEN).message("Forbidden 403").build();
        Menuitem menuitem = menuitemReq.convert();
        menuitemRepo.save(menuitem);
        subCategory.getMenuitemList().add(menuitem);
        restWithAdmin.getMenuitemList().add(menuitem);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success saved").build();
    }

    @Override
    public MenuitemRes getById(Long menuitemId) {
        String adEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(adEmail);
        Menuitem menuitem = myFindById(menuitemId);
        if (!restWithAdmin.getMenuitemList().contains(menuitem)) throw new ForbiddenException("Forbidden 403");
        return menuitem.convert();
    }

    @Override
    public MenuitemPagination getAll(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Menuitem> menuitemPage = menuitemRepo.findAllByRestaurantId(restWithAdmin.getId(),pageable);
        List<Menuitem> content = menuitemPage.getContent();
        List<MenuitemRes> menuitemRes = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
//            if (restWithAdmin.getMenuitemList().contains(content.get(i))) {
                menuitemRes.add(content.get(i).convert());
//            }
        }
        return MenuitemPagination.builder().page(menuitemPage.getNumber() + 1).
                size(menuitemPage.getTotalPages()).menuitemRes(menuitemRes).build();

    }

    @Override
    @Transactional
    public SimpleResponse delete(Long menuitemId) {
        String adEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(adEmail);
        Menuitem menuitem = myFindById(menuitemId);
        StopList stopList = stopListRepo.get(menuitem.getId());
        if (stopList != null) {
            stopListRepo.delete(stopList);
        }
        Cheque byMen = chequeRepo.getByMen(menuitemId);
        byMen.getMenuitemList().clear();
        if (!restWithAdmin.getMenuitemList().contains(menuitem)) throw new ForbiddenException("Forbidden 403");
        restWithAdmin.getMenuitemList().remove(menuitem);
        List<SubCategory> subCategoryList = subCategoryRepo.getByMenuitemId();
        for (int i = 0; i < subCategoryList.size(); i++) {
            if (subCategoryList.get(i).getMenuitemList().contains(menuitem)) {
                Long id = subCategoryList.get(i).getId();
                SubCategory subCategory = subCategoryRepo.findById(id).get();
                subCategory.getMenuitemList().remove(menuitem);
            }
        }
        menuitemRepo.delete(menuitem);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted!").build();
    }

    @Override
    @Transactional
    public SimpleResponse update(Long menuitemId, MenuitemReq menuitemReq) {
        String adEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(adEmail);
        Menuitem exFind = menuitemRepo.findByName(menuitemReq.getName(),restWithAdmin.getId());
        if (exFind != null)
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("This name already exists!").build();
        Menuitem menuitem = myFindById(menuitemId);
        if (!restWithAdmin.getMenuitemList().contains(menuitem)) throw new ForbiddenException("Forbidden 403");
        menuitem.setCount(menuitemReq.getCount());
        menuitem.setName(menuitemReq.getName());
        menuitem.setPrice(menuitemReq.getPrice());
        menuitem.setImage(menuitemReq.getImage());
        menuitem.setVegetarian(menuitemReq.isVegetarian());
        menuitem.setDescription(menuitemReq.getDescription());
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success updated").build();
    }

    @Override
    public List<MenuitemRes> search(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(adminEmail);
        String word = "%" + name + "%";
        return menuitemRepo.search(word, restWithAdmin.getId());
    }

    @Override
    public List<MenuitemRes> sortByPrice(String ascOrDesc) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        if (ascOrDesc.equalsIgnoreCase("asc")) {
            return restaurantRepo.sortByPrice(restWithAdmin.getId());
        } else if (ascOrDesc.equalsIgnoreCase("desc")) {
            return restaurantRepo.sortByPriceDesc(restWithAdmin.getId());
        }
        throw new RuntimeException("Write asc or desc");
    }

    @Override
    public List<MenuitemRes> filterByVega(boolean isVeg) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        return restaurantRepo.filterByVega(restWithAdmin.getId(), isVeg);
    }
}
