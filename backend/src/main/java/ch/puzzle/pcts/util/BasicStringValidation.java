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
 * This constraint is validated by
 * {@link ch.puzzle.pcts.util.BasicStringValidator}.
 *
 * <p>
 * The validation rules applied are:
 * <ul>
 * <li>The value must not be {@code null} (reports message
 * <code>{attribute.not.null}</code>).</li>
 * <li>The value must not be blank (i.e., empty or whitespace-only) (reports
 * message <code>{attribute.not.blank}</code>).</li>
 * <li>The length of the value must be between {@link #min()} and {@link #max()}
 * (inclusive) (reports message <code>{attribute.size.between}</code>).</li>
 * </ul>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { BasicStringValidator.class })
public @interface BasicStringValidation {

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

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link BasicStringValidation} annotations on the same
     * element. This enables the annotation to be repeatable.
     *
     * @see BasicStringValidation
     */
    @Target(FIELD)
    @Retention(RUNTIME)
    @Documented
    @interface List {
        BasicStringValidation[] value();
    }
}
