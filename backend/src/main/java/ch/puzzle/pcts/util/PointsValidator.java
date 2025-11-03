package ch.puzzle.pcts.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PointsValidator implements ConstraintValidator<PCTSPointsValidation, Number> {
    @Override
    public void initialize(PCTSPointsValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Number number, ConstraintValidatorContext context) {

        if (number == null) {
            return buildValidationFailure(context, "{attribute.not.null}");
        }

        if (number.doubleValue() < 0) {
            return buildValidationFailure(context, "{attribute.not.negative}");
        }
        return true;
    }

    private boolean buildValidationFailure(ConstraintValidatorContext context, String messageTemplate) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
        return false;
    }
}
