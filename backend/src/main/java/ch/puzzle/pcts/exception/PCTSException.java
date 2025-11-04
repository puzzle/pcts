package ch.puzzle.pcts.exception;

import ch.puzzle.pcts.dto.error.GenericErrorDto;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class PCTSException extends ResponseStatusException {
    private final List<GenericErrorDto> errors;

    public PCTSException(HttpStatusCode status, List<GenericErrorDto> errors) {
        super(status);
        this.errors = errors;
    }

    public List<GenericErrorDto> getErrorDto() {
        return errors;
    }
}
