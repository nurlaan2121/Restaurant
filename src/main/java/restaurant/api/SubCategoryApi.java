package restaurant.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.supcategory.SubCategoryRes;
import restaurant.entities.SubCategory;
import restaurant.service.SubCategoryService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/subcategory")
@RestController
public class SubCategoryApi {
    private final SubCategoryService subCategoryService;

    @PostMapping("/save/{catId}")
    public SimpleResponse save(@PathVariable Long catId, @RequestBody String name) {
        return subCategoryService.save(catId, name);
    }
    @GetMapping("/findById/{subCatId}")
    public SubCategoryRes getById(@PathVariable Long subCatId){
        return subCategoryService.getById(subCatId);
    }
    @DeleteMapping("/deleteById/{subCatId}")
    public SimpleResponse deleteById(@PathVariable Long subCatId){
        return subCategoryService.deleteById(subCatId);
    }
    @PutMapping("/update/{subCatId}")
    public SimpleResponse updateById(@PathVariable Long subCatId,@RequestBody String name){
        return subCategoryService.updateById(subCatId,name);
    }
    @GetMapping("/getAllSubCat/{catId}")
    public List<SubCategory> getAllSubCat(@PathVariable Long catId){
        return subCategoryService.getAllSubcat(catId);
    }
}
