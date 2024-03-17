package restaurant.api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.category.CategoryPagination;
import restaurant.dto.response.category.CategoryRes;
import restaurant.service.CategoryService;
@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
public class CategoryApi {
    private final CategoryService categoryService;
    @PostMapping("/save")
    public SimpleResponse save(@RequestBody String name){
        return categoryService.save(name);
    }
    @DeleteMapping("/deleteById/{categoryId}")
    public SimpleResponse deleteById(@PathVariable Long categoryId){
        return categoryService.deleteById(categoryId);
    }
    @PutMapping("/updateById{categoryId}")
    public SimpleResponse updateById(@PathVariable Long categoryId,String name){
        return categoryService.update(categoryId,name);
    }
    @GetMapping("/getMyCategories")
    public CategoryPagination getMyCats(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return categoryService.getMyCats(page,size);
    }
    @GetMapping("/findById/{categoryId}")
    public CategoryRes getById(@PathVariable Long categoryId){
      return   categoryService.getById(categoryId);
    }
}
