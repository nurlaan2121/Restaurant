package restaurant.service;

import restaurant.dto.request.ChequeReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.cheque.ChequeRes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public interface ChequeService {
    SimpleResponse save(ChequeReq chequeReq);

    ChequeRes getById(Long chequeId);

    BigDecimal getTotalSum(Long waiterId, LocalDate date);

    BigDecimal getAvg(LocalDate date);
}
