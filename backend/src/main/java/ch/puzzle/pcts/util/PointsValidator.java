package ch.puzzle.pcts.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PointsValidator implements ConstraintValidator<PointsValidation, Number> {
    @Override
    public void initialize(PointsValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Number number, ConstraintValidatorContext context) {

        if (number == null) {
            return buildViolation(context, "{attribute.not.null}");
        }

        if (number.doubleValue() < 0) {
            return buildViolation(context, "{attribute.not.negative}");
        }
        return true;
    }

    private boolean buildViolation(ConstraintValidatorContext context, String messageTemplate) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
        return false;
    }
}
