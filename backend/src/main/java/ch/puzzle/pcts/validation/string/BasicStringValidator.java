package ch.puzzle.pcts.validation.string;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BasicStringValidator implements ConstraintValidator<BasicStringValidation, String> {
    int min;
    int max;
    String message;

    @Override
    public void initialize(BasicStringValidation constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        if (string == null) {
            return buildViolation(context, "{attribute.not.null}");
        }

        if (string.isBlank()) {
            return buildViolation(context, "{attribute.not.blank}");
        }

        if (string.trim().length() < this.min || string.trim().length() > this.max) {
            return buildViolation(context, "{attribute.size.between}");
        }
        return true;
    }

    private boolean buildViolation(ConstraintValidatorContext context, String messageTemplate) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
        return false;
    }
}
