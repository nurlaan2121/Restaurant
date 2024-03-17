package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.UserResForPagination;
import restaurant.dto.response.category.CategoryPagination;
import restaurant.dto.response.category.CategoryRes;
import restaurant.entities.Category;
import restaurant.entities.Restaurant;
import restaurant.entities.User;
import restaurant.exceptions.ForbiddenException;
import restaurant.exceptions.NotFoundException;
import restaurant.repository.CategoryRepo;
import restaurant.repository.RestaurantRepo;
import restaurant.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final RestaurantRepo restaurantRepo;

    private Category myFindById(Long categoryId) {
        return categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("With category id not found:  " + categoryId));
    }

    @Override
    @Transactional
    public SimpleResponse save(String category) {
        if (category.length() < 2) {
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Write correct category ").build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        Category entityCat = new Category(category);
        categoryRepo.save(entityCat);
        restWithAdmin.getCategories().add(entityCat);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success saved").build();
    }

    @Override
    @Transactional
    public SimpleResponse deleteById(Long categoryId) {
        Category category = myFindById(categoryId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        if (!restWithAdmin.getCategories().contains(category))
            return SimpleResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("You no can deleted this category: " + categoryId).build();
        restWithAdmin.getCategories().remove(category);
        categoryRepo.delete(category);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted").build();
    }

    @Override
    @Transactional
    public SimpleResponse update(Long categoryId, String name) {
        Category category = myFindById(categoryId);
        category.setName(name);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success updated").build();
    }

    @Override
    public CategoryPagination getMyCats(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Category> catePage = categoryRepo.findAll(pageable);
        List<Category> content = catePage.getContent();
        List<CategoryRes> categoryPaginationList = new ArrayList<>();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        List<Category> categories = restWithAdmin.getCategories();
        for (int i = 0; i < content.size(); i++) {
            if (categories.contains(content.get(i))) {
                CategoryRes categoryRes = content.get(i).convert();
                categoryRes.setId(content.get(i).getId());
                categoryPaginationList.add(categoryRes);
            }
        }
        return CategoryPagination.builder().page(catePage.getNumber() + 1).
                size(catePage.getTotalPages()).categoryRes(categoryPaginationList).
                build();

    }

    @Override
    public CategoryRes getById(Long categoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        Category category = myFindById(categoryId);
        if (restWithAdmin.getCategories().contains(category)) {
            return category.convert();
        }
        throw new ForbiddenException("Forbidden 403 you no can see any category ");
    }
}
