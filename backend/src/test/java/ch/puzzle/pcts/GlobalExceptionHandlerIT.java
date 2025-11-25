package ch.puzzle.pcts;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;

class GlobalExceptionHandlerIT {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @DisplayName("Should return internal server error")
    @Test
    void shouldReturnInternalServerErrorResponse() {
        Exception ex = new Exception("Test exception");

        ResponseEntity<GenericErrorDto> response = handler.handleGenericException(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(500);

        GenericErrorDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.key()).isEqualTo(ErrorKey.INTERNAL);
        assertThat(body.values().values()).containsExactly();
    }

    @DisplayName("Should handle bind exception")
    @Test
    void shouldHandleBindException() {
        BindException ex = new BindException("Test exception", "Test bind exception");

        ResponseEntity<GenericErrorDto> response = handler.handle(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);

        GenericErrorDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.key()).isEqualTo(ErrorKey.VALIDATION);
        assertThat(body.values().values()).containsExactly();
    }

    @DisplayName("Should return pcts exception")
    @Test
    void shouldReturnPctsException() {
        PCTSException ex = new PCTSException(HttpStatus.BAD_REQUEST,
                                             List
                                                     .of(new GenericErrorDto(ErrorKey.INVALID_ARGUMENT,
                                                                             Map
                                                                                     .of(FieldKey.IS,
                                                                                         "Test Pcts exception"))));

        ResponseEntity<List<GenericErrorDto>> response = handler.handlePCTSException(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);

        GenericErrorDto body = response.getBody().get(0);
        assertThat(body).isNotNull();
        assertThat(body.key()).isEqualTo(ErrorKey.INVALID_ARGUMENT);
        assertThat(body.values().values()).containsExactly("Test Pcts exception");
    }
}
