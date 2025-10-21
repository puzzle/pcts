package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.FieldAwareMessageInterpolator;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.error.ErrorKey;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @param <T>
 *            the Type or entity of the repository
 */
@Service
public abstract class ValidationBase<T extends Model> {
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

    public void validateOnGet(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void validateOnCreate(T model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());

        validate(model);
    }

    public void validateOnUpdate(Long id, T model) {
        throwExceptionWhenIdIsNull(id);
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdHasChanged(id, model.getId());

        validate(model);
    }

    public void validateOnDelete(Long id) {
        throwExceptionWhenIdIsNull(id);
    }

    public void throwExceptionWhenIdIsNull(Long i) {
        if (i == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id must not be null.", ErrorKey.INVALID_ARGUMENT);
        }
    }

    public void throwExceptionWhenIdHasChanged(Long id, Long modelId) {
        if (!Objects.equals(id, modelId)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "The queried id must match the id in the model.",
                                    ErrorKey.INVALID_ARGUMENT);
        }
    }

    public void throwExceptionWhenIdIsNotNull(Long id) {
        if (id != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id must be null.", ErrorKey.INVALID_ARGUMENT);
        }
    }

    public void throwExceptionWhenModelIsNull(T model) {
        if (model == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Model must not be null.", ErrorKey.INVALID_ARGUMENT);
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
            // TODO: map her into the new ErrorDtos #145
            throw new PCTSException(HttpStatus.BAD_REQUEST, errorMessages, ErrorKey.INVALID_ARGUMENT);
        }
    }
}
