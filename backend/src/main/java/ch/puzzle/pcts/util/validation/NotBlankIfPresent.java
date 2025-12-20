package ch.puzzle.pcts.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.*;

/**
 * <h3>Validation rules:</h3>
 * <ul>
 * <li>The value can be {@code null}.</li>
 * <li>If the value is not {@code null}, it must contain at least one
 * non-whitespace character</li>
 * </ul>
 *
 * <p>
 * This constraint ensures that a field is "optional" (allows {@code null}) but
 * does not allow empty strings or strings consisting solely of whitespace.
 * </p>
 */
@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
// The regex \S ensures at least one non-whitespace character exists
@Pattern(regexp = ".*\\S.*", message = "{attribute.not.blank}")
public @interface NotBlankIfPresent {
    String message() default "attribute.not.blank";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
