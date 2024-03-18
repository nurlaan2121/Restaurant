package restaurant.service;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.category.CategoryPagination;
import restaurant.dto.response.category.CategoryRes;

import java.util.List;

public interface CategoryService {
    SimpleResponse save(String category);

    SimpleResponse deleteById(Long categoryId);

    SimpleResponse update(Long categoryId,String name);

    CategoryPagination getMyCats(int page,int size);

    CategoryRes getById(Long categoryId);

    List<CategoryRes> search(String name);
}
