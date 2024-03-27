package restaurant.api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.CategoryReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.category.CategoryPagination;
import restaurant.dto.response.category.CategoryRes;
import restaurant.service.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
public class CategoryApi {
    private final CategoryService categoryService;
    @Secured("ADMIN")
    @PostMapping("/save")
    public SimpleResponse save(@RequestBody @Valid CategoryReq name){
        return categoryService.save(name.name());
    }
    @Secured("ADMIN")
    @DeleteMapping("/deleteById/{categoryId}")
    public SimpleResponse deleteById(@PathVariable Long categoryId){
        return categoryService.deleteById(categoryId);
    }

    @Secured("ADMIN")
    @PutMapping("/updateById{categoryId}")
    public SimpleResponse updateById(@PathVariable Long categoryId,@RequestBody @Valid CategoryReq name){
        return categoryService.update(categoryId,name.name());
    }
    @Secured("ADMIN")
    @GetMapping("/getMyCategories")
    public CategoryPagination getMyCats(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return categoryService.getMyCats(page,size);
    }
    @Secured("ADMIN")
    @GetMapping("/findById/{categoryId}")
    public CategoryRes getById(@PathVariable Long categoryId){
      return categoryService.getById(categoryId);
    }
    @Secured("ADMIN")
    @GetMapping("/globalSearch")
    public List<CategoryRes> search(@RequestParam String name){
       return categoryService.search(name);
    }

}
