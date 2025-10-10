package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <R>
 *            the Repository of the entity
 * @param <P>
 *            the Persistence Service of this repository and entity
 */
public abstract class ValidationBase<T, R extends JpaRepository<T, Long>, P extends RolePersistenceService> {
    private final Validator validator;
    private final P persistenceService;

    ValidationBase(P persistenceService) {
        this.persistenceService = persistenceService;
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
            throw new PCTSException(HttpStatus.BAD_REQUEST, ErrorKey.INVALID_ARGUMENT);
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
