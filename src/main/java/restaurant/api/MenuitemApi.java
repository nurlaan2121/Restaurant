package restaurant.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.MenuitemReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.menuitem.MenuitemPagination;
import restaurant.dto.response.menuitem.MenuitemRes;
import restaurant.service.MenuitemService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/menuitem")
@RestController

public class MenuitemApi {
    private final MenuitemService menuitemService;
    @Secured({"ADMIN","CHEF"})
    @PostMapping("/save")
    public SimpleResponse save(@RequestBody @Valid MenuitemReq menuitemReq){
        return menuitemService.save(menuitemReq);
    }
    @Secured("ADMIN")
    @GetMapping("/findById/{menuitemId}")
    public MenuitemRes findById(@PathVariable Long menuitemId){
        return menuitemService.getById(menuitemId);
    }
    @Secured("ADMIN")
    @GetMapping("/getAll")
    public MenuitemPagination getAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return menuitemService.getAll(page,size);
    }
    @Secured("ADMIN")
    @DeleteMapping("deleteById/{menuitemId}")
    public SimpleResponse deleteById(@PathVariable Long menuitemId){
        return menuitemService.delete(menuitemId);
    }
    @Secured("ADMIN")
    @PutMapping("/update/{menuitemId}")
    public SimpleResponse updateById(@PathVariable Long menuitemId,@RequestBody @Valid MenuitemReq menuitemReq){
        return menuitemService.update(menuitemId,menuitemReq);
    }
    @Secured("ADMIN")
    @GetMapping("/globalSearch")
    public List<MenuitemRes> search(@RequestParam String name){
        return menuitemService.search(name);
    }
    @Secured("ADMIN")
    @GetMapping("/sortByPrice")
    public List<MenuitemRes> sortByPrice(@RequestParam String ascOrDesc){
        return menuitemService.sortByPrice(ascOrDesc);
    }
    @Secured("ADMIN")
    @GetMapping("/filterByVega")
    public List<MenuitemRes> filterByVega(@RequestParam boolean isVeg){
        return menuitemService.filterByVega(isVeg);
    }

}
