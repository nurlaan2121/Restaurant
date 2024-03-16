package restaurant.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restaurant.service.StopListService;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class StopListApi {
    private final StopListService stopListService;
}
