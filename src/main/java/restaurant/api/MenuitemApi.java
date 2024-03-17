package restaurant.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.MenuitemReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.menuitem.MenuitemPagination;
import restaurant.dto.response.menuitem.MenuitemRes;
import restaurant.service.MenuitemService;

@RequiredArgsConstructor
@RequestMapping("/api/menuitem")
@RestController

public class MenuitemApi {
    private final MenuitemService menuitemService;
    @PostMapping("/save")
    public SimpleResponse save(@RequestBody @Valid MenuitemReq menuitemReq){
        return menuitemService.save(menuitemReq);
    }
    @GetMapping("/findById/{menuitemId}")
    public MenuitemRes findById(@PathVariable Long menuitemId){
        return menuitemService.getById(menuitemId);
    }
    @GetMapping("/getAll")
    public MenuitemPagination getAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return menuitemService.getAll(page,size);
    }
    @DeleteMapping("deleteById/{menuitemId}")
    public SimpleResponse deleteById(@PathVariable Long menuitemId){
        return menuitemService.delete(menuitemId);
    }
    @PutMapping("update/{menuitemId}")
    public SimpleResponse updateById(@PathVariable Long menuitemId,@RequestBody @Valid MenuitemReq menuitemReq){
        return menuitemService.update(menuitemId,menuitemReq);
    }

}
