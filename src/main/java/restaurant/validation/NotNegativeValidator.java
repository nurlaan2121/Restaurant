package restaurant.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNegativeValidator implements ConstraintValidator<NotNegative, Long> {
    @Override
    public boolean isValid(Long number, ConstraintValidatorContext constraintValidatorContext) {
        return number>0;
    }
}
