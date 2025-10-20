package ch.puzzle.pcts.validation.basicstringvalidation;

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
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null) {
            return buildViolation(context, "{attribute.notnull}");
        }

        if (name.isBlank()) {
            return buildViolation(context, "{attribute.notblank}");
        }

        if (name.trim().length() < this.min || name.trim().length() > this.max) {
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
