package restaurant.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
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
    @Secured("ADMIN")
    @PostMapping("/save/{catId}")
    public SimpleResponse save(@PathVariable Long catId, @RequestBody String name) {
        return subCategoryService.save(catId, name);
    }
    @Secured("ADMIN")
    @GetMapping("/findById/{subCatId}")
    public SubCategoryRes getById(@PathVariable Long subCatId){
        return subCategoryService.getById(subCatId);
    }
    @Secured("ADMIN")
    @DeleteMapping("/deleteById/{subCatId}")
    public SimpleResponse deleteById(@PathVariable Long subCatId){
        return subCategoryService.deleteById(subCatId);
    }
    @Secured("ADMIN")
    @PutMapping("/update/{subCatId}")
    public SimpleResponse updateById(@PathVariable Long subCatId,@RequestBody String name){
        return subCategoryService.updateById(subCatId,name);
    }
    @Secured("ADMIN")
    @GetMapping("/getAllSubCat/{catId}")
    public List<SubCategory> getAllSubCat(@PathVariable Long catId){
        return subCategoryService.getAllSubcat(catId);
    }
    @Secured("ADMIN")
    @GetMapping("/globalSearch")
    public List<SubCategoryRes> search(@RequestParam String word){
        return subCategoryService.search(word);
    }
}
