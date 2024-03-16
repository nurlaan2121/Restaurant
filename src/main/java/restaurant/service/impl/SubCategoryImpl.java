package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restaurant.repository.SubCategoryRepo;
import restaurant.service.SubCategoryService;
@Service
@RequiredArgsConstructor
public class SubCategoryImpl implements SubCategoryService {
    private final SubCategoryRepo subCategoryRepo;
}
