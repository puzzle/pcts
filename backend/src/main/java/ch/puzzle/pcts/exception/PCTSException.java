package ch.puzzle.pcts.exception;

import ch.puzzle.pcts.model.error.ErrorKey;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class PCTSException extends ResponseStatusException {
    private final ErrorKey errorKey;

    public PCTSException(HttpStatusCode status, ErrorKey key) {
        super(status);
        this.errorKey = key;
    }

    public PCTSException(HttpStatusCode status, String reason, ErrorKey key) {
        super(status, reason);
        this.errorKey = key;
    }

    public ErrorKey getErrorKey() {
        return errorKey;
    }
}
