package restaurant.api;

import io.swagger.annotations.ApiParam;
import jdk.dynalink.linker.support.SimpleLinkRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.ChequeReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.cheque.ChequeRes;
import restaurant.service.ChequeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@RequestMapping("/api/cheque")
@RestController
public class ChequeApi {
    private final ChequeService chequeService;

    @Secured("WAITER")
    @PostMapping("/save")
    public SimpleResponse save(@RequestBody ChequeReq chequeReq) {
        return chequeService.save(chequeReq);
    }

    @Secured({"ADMIN","WAITER"})
    @GetMapping("/findById{chequeId}")
    public ChequeRes getById(@PathVariable Long chequeId) {
        return chequeService.getById(chequeId);
    }
    @Secured("ADMIN")
    @GetMapping("/getTotalSumOneDay/{waiterId}/{date}")
    public BigDecimal getTotalSum(@PathVariable Long waiterId, @PathVariable LocalDate date) {
        return chequeService.getTotalSum(waiterId,date);
    }
    @Secured("ADMIN")
    @GetMapping("/getAvgSum/{date}")
    public BigDecimal getAvg(@PathVariable LocalDate date) {
        return chequeService.getAvg(date);
    }
    @Secured("ADMIN")
    @PutMapping("/updateCheck/{checkId}")
    public SimpleResponse update(@PathVariable Long checkId,@RequestBody ChequeReq chequeReq){
        return chequeService.update(checkId,chequeReq);
    }
}
