package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restaurant.repository.ChequeRepo;
import restaurant.service.CategoryService;
import restaurant.service.ChequeService;

@Service
@RequiredArgsConstructor
public class ChequeImpl implements ChequeService {
    private final ChequeRepo chequeRepo;
}
