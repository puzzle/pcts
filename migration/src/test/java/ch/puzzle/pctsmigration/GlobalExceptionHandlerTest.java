package ch.puzzle.pctsmigration;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.client.ApiException;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("It should return an HTTP 422 (Unprocessable Entity) response with the title “Validation Error”")
    void handleIllegalArgumentException_returns422WithValidationTitle() {
        String errorMessage = "Ungültiges Zertifikatsdatum";
        IllegalArgumentException ex = new IllegalArgumentException(errorMessage);

        ProblemDetail result = exceptionHandler.handleIllegalArgumentException(ex);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(422);
        assertThat(result.getTitle()).isEqualTo("Validation Error");
        assertThat(result.getDetail()).isEqualTo(errorMessage);
    }


    @Test
    @DisplayName("Should return an HTTP 502 (Bad Gateway) error with the title “AI Service Error”")
    void handleApiException_returns502WithAiServiceTitle() {
        String errorMessage = "API antwortet nicht";
        ApiException ex = new ApiException(errorMessage);

        ProblemDetail result = exceptionHandler.handleApiException(ex);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(502);
        assertThat(result.getTitle()).isEqualTo("AI Service Error");
        assertThat(result.getDetail()).isEqualTo(errorMessage);
    }


    @Test
    @DisplayName("It should return an HTTP 400 (Bad Request) error and the title “File Processing Error”")
    void handleIOException_returns400WithFileProcessingTitle() {
        String errorMessage = "Datei konnte nicht gelesen werden";
        IOException ex = new IOException(errorMessage);

        ProblemDetail result = exceptionHandler.handleIOException(ex);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getTitle()).isEqualTo("File Processing Error");
        assertThat(result.getDetail()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("It should return an HTTP 500 (Internal Server Error) and the title “Internal Server Error”")
    void handleGlobalException_returns500WithInternalServerTitle() {
        String errorMessage = "Unerwarteter NullPointer";
        Exception ex = new NullPointerException(errorMessage);

        ProblemDetail result = exceptionHandler.handleGlobalException(ex);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getTitle()).isEqualTo("Internal Server Error");
        assertThat(result.getDetail()).isEqualTo(errorMessage);
    }
}
