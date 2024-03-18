package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.supcategory.SubCategoryRes;
import restaurant.entities.Category;
import restaurant.entities.Restaurant;
import restaurant.entities.SubCategory;
import restaurant.exceptions.ForbiddenException;
import restaurant.exceptions.NotFoundException;
import restaurant.repository.CategoryRepo;
import restaurant.repository.RestaurantRepo;
import restaurant.repository.SubCategoryRepo;
import restaurant.service.SubCategoryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryImpl implements SubCategoryService {

    private final SubCategoryRepo subCategoryRepo;
    private final CategoryRepo categoryRepo;
    private final RestaurantRepo restaurantRepo;

    private Category myCatFindById(Long catId) {
        return categoryRepo.findById(catId).orElseThrow(() -> new NotFoundException("With category not found" + catId));
    }

    private SubCategory mySubCatFindById(Long supCatId) {
        return subCategoryRepo.findById(supCatId).orElseThrow(() -> new NotFoundException("With Sub Category not found" + supCatId));
    }

    @Override
    @Transactional
    public SimpleResponse save(Long catId, String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Category category = myCatFindById(catId);
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        if (!restWithAdmin.getCategories().contains(category))
            return SimpleResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("Forbidden 403").build();
        if (subCategoryRepo.existsByName(name, category.getId())) {
            return SimpleResponse.builder().httpStatus(HttpStatus.CONFLICT).message("This name already exists!  :" + name).build();
        }
        SubCategory subCategory = new SubCategory(name);
        subCategoryRepo.save(subCategory);
        category.getSubCategories().add(subCategory);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success saved").build();
    }

    @Override
    public SubCategoryRes getById(Long subCatId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SubCategory subCategory = mySubCatFindById(subCatId);
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        Category category = categoryRepo.getCatByResId(restWithAdmin.getId());
        for (int i = 0; i < category.getSubCategories().size(); i++) {
            if (!category.getSubCategories().contains(subCategory))
                throw new ForbiddenException("Forbidden 403");
        }
        return subCategory.convert();
    }

    @Override
    @Transactional
    public SimpleResponse deleteById(Long subCatId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SubCategory subCategory = mySubCatFindById(subCatId);
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        Category category = categoryRepo.getCatByResId(restWithAdmin.getId());
        for (int i = 0; i < category.getSubCategories().size(); i++) {
            if (!category.getSubCategories().contains(subCategory))
                throw new ForbiddenException("Forbidden 403");
            category.getSubCategories().remove(subCategory);
            subCategoryRepo.deleteById(subCatId);

        }
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted!").build();
    }

    @Override
    @Transactional
    public SimpleResponse updateById(Long subCatId, String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SubCategory subCategory = mySubCatFindById(subCatId);
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        Category category = categoryRepo.getCatByResId(restWithAdmin.getId());
        for (int i = 0; i < category.getSubCategories().size(); i++) {
            if (!category.getSubCategories().contains(subCategory))
                throw new ForbiddenException("Forbidden 403");
            if (subCategoryRepo.existsByName(name, category.getId())) {
                return SimpleResponse.builder().httpStatus(HttpStatus.CONFLICT).message("This name already exists!  :" + name).build();
            }
            subCategory.setName(name);
        }
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted!").build();

    }

    @Override
    public List<SubCategory> getAllSubcat(Long catId) {
        Category category = myCatFindById(catId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
        if (!restWithAdmin.getCategories().contains(category)) throw new ForbiddenException("Forbidden 403");
        return subCategoryRepo.sortByName(catId);
    }

    @Override
    public List<SubCategoryRes> search(String word) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAdmin = authentication.getName();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(emailAdmin);
        List<Category> categories = restWithAdmin.getCategories();
        List<SubCategoryRes> resList = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            Category category = categoryRepo.findById(categories.get(i).getId()).get();
            for (int i1 = 0; i1 < category.getSubCategories().size(); i1++) {
                Long subCatId = category.getSubCategories().get(i1).getId();
                SubCategory subCategory = mySubCatFindById(subCatId);
                if (subCategory.getName().contains(word)) {
                    resList.add(subCategory.convert());
                }
            }
        }
        return resList;
    }
}
