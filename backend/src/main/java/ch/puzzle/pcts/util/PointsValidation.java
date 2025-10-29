package ch.puzzle.pcts.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Validates that a numeric field representing points is not {@code null} and
 * not negative.
 * <p>
 * This annotation can be applied to numeric fields to ensure that their value
 * is greater than or equal to zero. The validation logic is implemented in
 * {@link PointsValidator}.
 * </p>
 *
 * <h3>Validation rules:</h3>
 * <ul>
 * <li>The value must not be {@code null} (reports message
 * <code>{attribute.not.null}</code>).</li>
 * <li>The value must be greater than or equal to zero (reports * message
 * <code>{attribute.not.negative}</code>).</li>
 * </ul>
 *
 * <p>
 * If validation fails, the default message template {@code "{class}.{field} is
 * not valid"} will be used, but this can be overridden by specifying a custom
 * message via the {@link #message()} attribute.
 * </p>
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PointsValidator.class)
public @interface PointsValidation {

    /**
     * The message that will be shown when the validation fails.
     *
     * @return the error message template
     */
    String message() default "{class}.{field} is not valid";

    /**
     * Allows specification of validation groups, to which this constraint belongs.
     *
     * @return an array of group classes
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients to assign custom payload objects to a constraint.
     *
     * @return an array of payload classes
     */
    Class<? extends Payload>[] payload() default {};
}
