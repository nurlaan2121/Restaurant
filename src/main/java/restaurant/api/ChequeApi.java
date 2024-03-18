package restaurant.api;

import jdk.dynalink.linker.support.SimpleLinkRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import restaurant.dto.request.ChequeReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.cheque.ChequeRes;
import restaurant.service.ChequeService;

import java.math.BigDecimal;

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
    @GetMapping("/getTotalSumOneDay/{waiterId}")
    public BigDecimal getTotalSum(@PathVariable Long waiterId) {
        return chequeService.getTotalSum(waiterId);
    }

    @Secured("ADMIN")
    @GetMapping("/getAvgSum")
    public BigDecimal getAvg() {
        return chequeService.getAvg();
    }
}
