package restaurant.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class NotNegativeBigValidator implements ConstraintValidator<NotNegativeBig, BigDecimal> {
    @Override
    public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext constraintValidatorContext) {
        return bigDecimal.longValue() > 0L;
    }
}
