package ch.puzzle.pcts.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.lang.annotation.*;

/**
 * <h3>Validation rules:</h3>
 * <ul>
 * <li>The value must not be {@code null} (reports message
 * <code>{attribute.not.null}</code>).</li>
 * <li>The value must be greater than or equal to zero (reports message
 * <code>{attribute.not.negative}</code>).</li>
 * </ul>
 *
 * <p>
 * This composed constraint combines {@link NotNull} and {@link PositiveOrZero}
 * to ensure that a value is present and non-negative.
 * </p>
 */
@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "{attribute.not.null}")
@PositiveOrZero(message = "{attribute.not.negative}")
public @interface PCTSPointsValidation {

    String message() default "{class}.{field} is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}