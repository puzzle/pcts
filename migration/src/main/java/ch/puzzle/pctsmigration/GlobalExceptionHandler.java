package ch.puzzle.pctsmigration;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MigrationException.class)
    public ResponseEntity<Error> handleGlobalException(MigrationException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getError());
    }
}