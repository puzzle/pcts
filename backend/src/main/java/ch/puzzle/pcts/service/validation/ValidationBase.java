package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.util.FieldAwareMessageInterpolator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @param <T>
 *            the type or entity of the repository
 */
@Service
public abstract class ValidationBase<T extends Model> {
    private final Validator validator;

    @Autowired
    private Environment environment;

    protected ValidationBase() {
        final ValidatorFactory factory = Validation
                .byDefaultProvider()
                .configure()
                .messageInterpolator(new FieldAwareMessageInterpolator(Validation
                        .byDefaultProvider()
                        .configure()
                        .getDefaultMessageInterpolator()))
                .buildValidatorFactory();
        try (factory) {
            validator = factory.getValidator();
        }
    }

    public void validateOnGetById(Long id) {
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
            throw new PCTSException(HttpStatus.BAD_REQUEST, buildGenericErrorDto("id can not be null", Map.of()));
        }
    }

    private void throwExceptionWhenIdHasChanged(Long id, Long modelId) {
        if (modelId != null && !Objects.equals(id, modelId)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, buildGenericErrorDto("id has changed", Map.of()));
        }
    }

    private void throwExceptionWhenIdIsNotNull(Long id) {
        if (id != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, buildGenericErrorDto("id has to be null", Map.of()));
        }
    }

    private void throwExceptionWhenModelIsNull(T model) {
        if (model == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, buildGenericErrorDto("model can not be null", Map.of()));
        }
    }

    public void validate(T member) {
        Set<ConstraintViolation<T>> violations = validator.validate(member);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            List<GenericErrorDto> genericErrorDtos = violations
                    .stream()
                    .map(violation -> new GenericErrorDto(getAttribute("key", violation.getMessage()),
                                                          Map.of("test", "test")))
                    .toList();
            throw new PCTSException(HttpStatus.BAD_REQUEST, genericErrorDtos);
        }
    }

    private static String getAttribute(String searchedAttribute, String errorMessage) {

        System.out.println("error message: " + errorMessage);

        if (searchedAttribute == null || searchedAttribute.isEmpty() || errorMessage == null
            || errorMessage.isEmpty()) {
            return null;
        }

        String quotedKey = Pattern.quote(searchedAttribute);

        String regex = String.format("%s\\s*=\\s*(.*?)\\s*(?:,\\s*[\\w\\.]+\\s*=|$)", quotedKey);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(errorMessage);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;

    }

    public List<GenericErrorDto> buildGenericErrorDto(String key, Map<String, String> errors) {
        return List.of(new GenericErrorDto(key, errors));
    }
}
