package restaurant.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.RestaurantReq;
import restaurant.dto.request.UpdateRestReq;
import restaurant.dto.response.*;
import restaurant.enums.ActionForReq;
import restaurant.enums.RestaurantType;
import restaurant.service.RestaurantService;
import restaurant.validation.PasswordValidation;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantApi {
    private final RestaurantService restaurantService;
    @Secured("DEV")
    @PostMapping("/save")
    public SimpleResponse save(@RequestBody @Valid  RestaurantReq restaurantReq ,@RequestParam RestaurantType restaurantType){
        return restaurantService.save(restaurantReq,restaurantType);
    }
    @Secured("DEV")
    @GetMapping("/getAll")
    public RestaurantPagination getAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return restaurantService.getAll(page,size);
    }
    @GetMapping("/findById/{restId}")
    public RestaurantResponse findById(@PathVariable Long restId){
        return restaurantService.findById(restId);
    }
    @Secured("ADMIN")
    @GetMapping("/getMyRestaurant")
    public RestaurantResponse findById(){
        return restaurantService.findById();
    }
    @Secured("DEV")
    @DeleteMapping("/deleteById/{resId}")
    public SimpleResponse deleteById(@PathVariable Long resId){
        return restaurantService.deleteById(resId);
    }
    @Secured("ADMIN")
    @DeleteMapping("/deleteById")
    public SimpleResponse delete(){
        return restaurantService.delete();
    }
    @Secured("ADMIN")
    @PutMapping("/update")
    public SimpleResponse update(@RequestBody @Valid UpdateRestReq updateRestReq,@RequestParam RestaurantType restaurantType){
        return restaurantService.updateById(updateRestReq,restaurantType);
    }
    @Secured("DEV")
    @PutMapping("/update{restId}")
    public SimpleResponse update(@PathVariable Long restId,@RequestBody @Valid UpdateRestReq updateRestReq,@RequestParam RestaurantType restaurantType){
        return restaurantService.updateById(restId,updateRestReq,restaurantType);
    }
    @Secured("ADMIN")
    @GetMapping("getAllRequests")
    public PaginationUserReq getAllReq(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size){
        return restaurantService.getAllReq(page,size);
    }
    @Secured("ADMIN")
    @PutMapping("requestAccOrRem/{reqId}")
    public SimpleResponse reqAcceptOrRemove(@PathVariable Long reqId,@RequestParam(defaultValue = "ACCEPT") ActionForReq action){
       return restaurantService.reqAccOrRem(reqId,action);
    }
}
