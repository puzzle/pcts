package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.util.FieldAwareMessageInterpolator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @param <T>
 *            the type or entity of the repository
 */
@Service
public abstract class ValidationBase<T extends Model> implements ValidationService<T> {
    private static final Logger log = LoggerFactory.getLogger(ValidationBase.class);
    private final Validator validator;

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

    private void throwExceptionWhenIdIsNull(Long id) {
        if (id == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    buildGenericErrorDto(ErrorKey.VALIDATION, Map.of(FieldKey.FIELD, "id")));
        }
    }

    private void throwExceptionWhenIdHasChanged(Long id, Long modelId) {
        if (modelId != null && !Objects.equals(id, modelId)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    buildGenericErrorDto(ErrorKey.VALIDATION, Map.of(FieldKey.FIELD, "id")));
        }
    }

    private void throwExceptionWhenIdIsNotNull(Long id) {
        if (id != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    buildGenericErrorDto(ErrorKey.VALIDATION, Map.of(FieldKey.FIELD, "id")));
        }
    }

    private void throwExceptionWhenModelIsNull(T model) {
        if (model == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    buildGenericErrorDto(ErrorKey.VALIDATION, Map.of(FieldKey.FIELD, "id")));
        }
    }

    public void validate(T member) {
        Set<ConstraintViolation<T>> violations = validator.validate(member);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            List<GenericErrorDto> errors = violations
                    .stream()
                    .map(violation -> parseViolationMessage(violation.getMessage()))
                    .toList();
            throw new PCTSException(HttpStatus.BAD_REQUEST, errors);
        }
    }

    private static GenericErrorDto parseViolationMessage(String message) {
        Map<String, String> valueMap = parseToMap(message);

        Optional<String> keyNameOpt = Optional.ofNullable(valueMap.remove("key"));

        if (keyNameOpt.isEmpty()) {
            log.error("Validation message is missing 'key=' part: {}", message);
            return new GenericErrorDto(ErrorKey.ERROR_MESSAGE_MISSING_KEY, Map.of(FieldKey.IS, message));
        }

        Optional<ErrorKey> errorKeyOpt = parseErrorKey(keyNameOpt.get());

        if (errorKeyOpt.isEmpty()) {
            return new GenericErrorDto(ErrorKey.ERROR_MESSAGE_INVALID_KEY, Map.of(FieldKey.IS, keyNameOpt.get()));
        }

        Map<FieldKey, String> fieldMap = new EnumMap<>(FieldKey.class);

        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            Optional<FieldKey> fieldKeyOpt = parseFieldKey(entry.getKey());

            if (fieldKeyOpt.isEmpty()) {
                return new GenericErrorDto(ErrorKey.ERROR_MESSAGE_INVALID_KEY, Map.of(FieldKey.IS, entry.getKey()));
            }
            fieldMap.put(fieldKeyOpt.get(), entry.getValue());
        }

        return new GenericErrorDto(errorKeyOpt.get(), fieldMap);
    }

    private static Optional<ErrorKey> parseErrorKey(String key) {
        try {
            return Optional.of(ErrorKey.valueOf(key));
        } catch (IllegalArgumentException e) {
            log.error("ErrorKey enum does not contain key '{}' from ValidationMessages.properties!", key, e);
            return Optional.empty();
        }
    }

    private static Optional<FieldKey> parseFieldKey(String key) {
        try {
            return Optional.of(FieldKey.valueOf(key.toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.error("FieldKey enum does not contain key '{}'", key, e);
            return Optional.empty();
        }
    }

    private static Map<String, String> parseToMap(String message) {
        message = message.replaceAll("\\s", "");
        return Arrays
                .stream(message.split(","))
                .map(entry -> entry.split("="))
                .filter(entry -> entry.length > 1)
                .collect(Collectors.toMap(entry -> entry[0].toLowerCase(), entry -> entry[1]));
    }

    protected static List<GenericErrorDto> buildGenericErrorDto(ErrorKey key, Map<FieldKey, String> errors) {
        return List.of(new GenericErrorDto(key, errors));
    }

    protected void validateDateIsBefore(String entity, String earlierFieldName, LocalDate earlierDate,
                                        String laterFieldName, LocalDate laterDate) {

        if (earlierDate != null && laterDate != null && earlierDate.isAfter(laterDate)) {

            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        entity,
                        FieldKey.FIELD,
                        earlierFieldName,
                        FieldKey.IS,
                        earlierDate.toString(),
                        FieldKey.CONDITION_FIELD,
                        laterFieldName,
                        FieldKey.MAX,
                        laterDate.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_NOT_BEFORE, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
