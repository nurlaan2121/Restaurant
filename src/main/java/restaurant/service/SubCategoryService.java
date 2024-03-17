package restaurant.service;

import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.supcategory.SubCategoryRes;
import restaurant.entities.SubCategory;

import java.util.List;

public interface SubCategoryService {
    SimpleResponse save(Long catId,String name);

    SubCategoryRes getById(Long subCatId);

    SimpleResponse deleteById(Long subCatId);

    SimpleResponse updateById(Long subCatId,String name);

    List<SubCategory> getAllSubcat(Long catId);
}
