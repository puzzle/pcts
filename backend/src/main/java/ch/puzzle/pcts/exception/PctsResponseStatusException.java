package ch.puzzle.pcts.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class PctsResponseStatusException extends ResponseStatusException {
    public PctsResponseStatusException(HttpStatusCode status) {
        super(status);
    }

    public PctsResponseStatusException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
