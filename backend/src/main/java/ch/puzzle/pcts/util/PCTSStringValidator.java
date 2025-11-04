package ch.puzzle.pcts.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PCTSStringValidator implements ConstraintValidator<PCTSStringValidation, String> {
    private PCTSStringValidation annotation;

    @Override
    public void initialize(PCTSStringValidation constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return buildValidationFailure(context, "{attribute.not.null}");
        }

        if (value.isBlank()) {
            return buildValidationFailure(context, "{attribute.not.blank}");
        }

        if (value.length() < annotation.min() || value.length() > annotation.max()) {
            return buildValidationFailure(context, "{attribute.size.between}");
        }
        return true;
    }

    private boolean buildValidationFailure(ConstraintValidatorContext context, String messageTemplate) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
        return false;
    }
}
