package ch.puzzle.pcts.exception;

import ch.puzzle.pcts.model.error.ErrorKey;
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

    public PCTSException(HttpStatusCode status, ErrorKey errorKey, Map<String, String> error) {
        super(status);
        this.errors = List.of(new GenericError(errorKey, error));
    }

    public List<GenericError> getErrors() {
        return errors;
    }

    // TODO: remove me
    public ErrorKey getErrorKey() {
        return ErrorKey.ATTRIBUTE_DATE_PAST;
    }
}
