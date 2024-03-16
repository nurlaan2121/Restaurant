package restaurant.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.RestaurantReq;
import restaurant.dto.request.UpdateRestReq;
import restaurant.dto.response.RestaurantPagination;
import restaurant.dto.response.RestaurantResponse;
import restaurant.dto.response.SimpleResponse;
import restaurant.enums.RestaurantType;
import restaurant.service.RestaurantService;
import restaurant.validation.PasswordValidation;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantApi {
    private final RestaurantService restaurantService;
    @PostMapping("/save")
    public SimpleResponse save(@RequestBody @Valid  RestaurantReq restaurantReq ,@RequestParam RestaurantType restaurantType){
        return restaurantService.save(restaurantReq,restaurantType);
    }
    @GetMapping("/getAll")
    public RestaurantPagination getAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return restaurantService.getAll(page,size);
    }
    @GetMapping("/findById/{restId}")
    public RestaurantResponse findById(@PathVariable Long restId){
        return restaurantService.findById(restId);
    }
    @DeleteMapping("/deleteById/{resId}")
    public SimpleResponse deleteById(@PathVariable Long resId){
        return restaurantService.deleteById(resId);
    }
    @PutMapping("/update/{restId}")
    public SimpleResponse update(@PathVariable Long restId, @RequestBody UpdateRestReq updateRestReq,@RequestParam RestaurantType restaurantType){
        return restaurantService.updateById(restId,updateRestReq,restaurantType);
    }
    @GetMapping("/checkVacancy/{restId}")
    public String checkVacancy(@PathVariable Long restId){
        return restaurantService.checkVacancy(restId);
    }

}
