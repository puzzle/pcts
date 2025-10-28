package ch.puzzle.pcts.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicStringValidator implements ConstraintValidator<BasicStringValidation, String> {
    private BasicStringValidation annotation;

    @Override
    public void initialize(BasicStringValidation constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return buildViolation(context, "{attribute.not.null}");
        }

        if (value.isBlank()) {
            return buildViolation(context, "{attribute.not.blank}");
        }

        if (value.length() < annotation.min() || value.length() > annotation.max()) {
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
