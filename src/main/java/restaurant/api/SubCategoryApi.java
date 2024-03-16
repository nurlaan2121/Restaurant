package restaurant.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restaurant.service.SubCategoryService;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class SubCategoryApi {
    private final SubCategoryService subCategoryService;
}
