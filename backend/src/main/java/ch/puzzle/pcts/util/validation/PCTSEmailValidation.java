package ch.puzzle.pcts.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3>Validation rules:</h3>
 * <ul>
 * <li>The value can be {@code null}.</li>
 * <li>If the value is not {@code null}, it must be a valid email address
 * according to RFC 5322</li>
 * </ul>
 *
 * <p>
 * This constraint ensures that a field is "optional" (allows {@code null}) but
 * does not allow an invalid email address.
 * </p>
 */
@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = ".*\\S.*", message = "{attribute.not.blank}")
@Email(message = "{attribute.not.email}")
public @interface PCTSEmailValidation {
    String message() default "{class}.{field} is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
