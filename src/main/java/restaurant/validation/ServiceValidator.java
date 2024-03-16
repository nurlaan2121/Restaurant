package restaurant.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ServiceValidator implements ConstraintValidator<ServiceValidation, Long> {
    @Override
    public boolean isValid(Long servicePro, ConstraintValidatorContext constraintValidatorContext) {
        return servicePro > 0 && servicePro < 50;
    }
}
