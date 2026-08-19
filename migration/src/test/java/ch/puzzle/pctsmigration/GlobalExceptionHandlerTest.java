package ch.puzzle.pctsmigration;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("It should return an HTTP 500 (Internal Server Error) and the title “Internal Server Error”")
    void handleGlobalException_returns500WithInternalServerTitle() {
        Error error = new Error(HttpStatusCode.valueOf(400), "Internal Server Error");
        MigrationException ex = new MigrationException(error);

        ResponseEntity<Error> result = exceptionHandler.handleGlobalException(ex);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().message()).isEqualTo(error.message());
        assertThat(result.getStatusCode()).isEqualTo(error.status());
    }
}
