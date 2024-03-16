package restaurant.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restaurant.service.ChequeService;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class ChequeApi {
    private final ChequeService chequeService;
}
