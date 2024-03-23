package restaurant.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.StopListReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.service.StopListService;

@RequiredArgsConstructor
@RequestMapping("api/stopList")
@RestController
public class StopListApi {
    private final StopListService stopListService;
    @Secured({"ADMIN","CHEF"})
    @PutMapping("/updateStopList/{menuitemId}")
    public SimpleResponse update(@RequestParam Long count,@PathVariable Long menuitemId){
        return stopListService.update(count,menuitemId);
    }
    @Secured("ADMIN")
    @PutMapping("/updateReason/{stopListId}")
    public SimpleResponse updRea(@PathVariable Long stopListId,@RequestBody @Valid StopListReq newReason){
        return stopListService.updateReason(stopListId,newReason.name());

    }}
