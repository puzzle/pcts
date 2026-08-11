package ch.puzzle.pcts.util.validation;

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
            // returns true if null and nullable (is valid) to prevent further validations
            return annotation.nullable() || buildValidationFailure(context, "{attribute.not.null}");
        }

        if (value.isBlank()) {
            // returns true if blank and blank is allowed to prevent further validations
            return annotation.allowOnlyWhiteSpaces() || buildValidationFailure(context, "{attribute.not.blank}");
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
