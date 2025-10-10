package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

/**
 * @param <T>
 *            the Type or entity of the repository
 */
public abstract class ValidationBase<T> {
    private final Validator validator;

    ValidationBase() {
        try (ValidatorFactory factory = Validation
                .byDefaultProvider()
                .configure()
                .messageInterpolator(new FieldAwareMessageInterpolator(Validation
                        .byDefaultProvider()
                        .configure()
                        .getDefaultMessageInterpolator()))
                .buildValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public void validateOnGetById(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void validateOnCreate(T member) {
        validate(member);
    }

    public void validateOnDelete(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void validateOnUpdate(Long id, T member) {
        throwExceptionWhenIdIsNull(id);
        validate(member);
    }

    public void throwExceptionWhenIdIsNull(Long i) {
        if (i == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id must not be zero", ErrorKey.INVALID_ARGUMENT);
        }
    }

    public void validate(T member) {
        Set<ConstraintViolation<T>> violations = validator.validate(member);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            String errorMessages = violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            // TODO: handle it properly here
            throw new PCTSException(HttpStatus.BAD_REQUEST, errorMessages, ErrorKey.INVALID_ARGUMENT);
        }
    }
}
