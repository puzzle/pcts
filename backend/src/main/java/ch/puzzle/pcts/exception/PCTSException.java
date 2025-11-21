package ch.puzzle.pcts.exception;

import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.model.error.GenericError;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class PCTSException extends ResponseStatusException {
    private final List<GenericError> errors;

    public PCTSException(HttpStatusCode status, List<GenericError> errors) {
        super(status);
        this.errors = errors;
    }

    public PCTSException(HttpStatusCode status, ErrorKey errorKey, Map<FieldKey, String> error) {
        super(status);
        this.errors = List.of(new GenericError(errorKey, error));
    }

    public List<GenericError> getErrors() {
        return errors;
    }

    public List<ErrorKey> getErrorKeys() {
        return getErrors().stream().map(GenericError::getKey).toList();
    }

    public List<Map<FieldKey, String>> getErrorAttributes() {
        return getErrors().stream().map(GenericError::getValues).toList();
    }

}
