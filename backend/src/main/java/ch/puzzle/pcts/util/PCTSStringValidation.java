package ch.puzzle.pcts.util;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Validates that a {@link String} field is not {@code null}, not blank, and its
 * length falls within a specified range.
 *
 * <p>
 * This constraint is validated by {@link PCTSStringValidator}.
 * </p>
 *
 * <p>
 * The validation rules are evaluated in the following order:
 * <ol>
 * <li>Check that the value is not {@code null} (reports message
 * <code>{attribute.not.null}</code>).</li>
 * <li>Check that the value is not blank (i.e., not empty or whitespace-only)
 * (reports message <code>{attribute.not.blank}</code>).</li>
 * <li>Check that the length of the value is between {@link #min()} and
 * {@link #max()} (inclusive) (reports message
 * <code>{attribute.size.between}</code>).</li>
 * </ol>
 *
 * <p>
 * Only the <strong>first failing rule</strong> produces a violation. Once a
 * validation error is detected, subsequent checks are not evaluated.
 * </p>
 *
 * <p>
 * This behavior ensures that the reported violation message reflects the most
 * fundamental issue with the provided value.
 * </p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { PCTSStringValidator.class })
public @interface PCTSStringValidation {

    /**
     * @return The minimum allowed length for the string (inclusive).
     */
    int min() default 2;

    /**
     * @return The maximum allowed length for the string (inclusive).
     */
    int max() default 250;

    /**
     * @return The default error message template. (Note: The specific validator
     *         uses only messages like <code>{attribute.not.null}</code>,
     *         <code>{attribute.not.blank}</code>, and
     *         <code>{attribute.size.between}</code>).
     */
    String message() default "{validation.basicString.default}";

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

    /**
     * Defines several {@link PCTSStringValidation} annotations on the same element.
     * This enables the annotation to be repeatable.
     *
     * @see PCTSStringValidation
     */
    @Target(FIELD)
    @Retention(RUNTIME)
    @Documented
    @interface List {
        PCTSStringValidation[] value();
    }
}
