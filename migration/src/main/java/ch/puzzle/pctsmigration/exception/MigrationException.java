package ch.puzzle.pctsmigration.exception;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.server.ResponseStatusException;

public class MigrationException extends ResponseStatusException {
    private final Error error;

    public MigrationException(@NotNull Error error) {
        super(error.status());
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
