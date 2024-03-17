package restaurant.service;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.category.CategoryPagination;
import restaurant.dto.response.category.CategoryRes;
public interface CategoryService {
    SimpleResponse save(String category);

    SimpleResponse deleteById(Long categoryId);

    SimpleResponse update(Long categoryId,String name);

    CategoryPagination getMyCats(int page,int size);

    CategoryRes getById(Long categoryId);
}
