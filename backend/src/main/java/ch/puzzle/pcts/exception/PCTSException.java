package ch.puzzle.pcts.exception;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class PCTSException extends ResponseStatusException {
    private final List<GenericErrorDto> errors;

    public PCTSException(HttpStatusCode status, List<GenericErrorDto> errors) {
        super(status);
        this.errors = errors;
    }

    public List<GenericErrorDto> getErrors() {
        return errors;
    }

    public List<ErrorKey> getErrorKeys() {
        return getErrors().stream().map(GenericErrorDto::key).toList();
    }

    public List<Map<FieldKey, String>> getErrorAttributes() {
        return getErrors().stream().map(GenericErrorDto::values).toList();
    }
}
