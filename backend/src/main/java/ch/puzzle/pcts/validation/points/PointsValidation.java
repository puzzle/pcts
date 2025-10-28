package ch.puzzle.pcts.validation.points;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PointsValidator.class)
public @interface PointsValidation {
    String message() default "{class}.{field} is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}