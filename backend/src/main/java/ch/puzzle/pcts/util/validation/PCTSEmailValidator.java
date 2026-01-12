package ch.puzzle.pcts.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PCTSEmailValidator implements ConstraintValidator<PCTSEmailValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{attribute.not.blank}").addConstraintViolation();
            return false;
        }

        return true;
    }
}
