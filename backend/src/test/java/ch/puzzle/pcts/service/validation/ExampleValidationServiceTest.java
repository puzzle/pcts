package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.persistence.ExamplePersistenceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ExampleValidationServiceTest {
    private AutoCloseable closeable;

    @Mock
    private ExamplePersistenceService persistenceService;

    @InjectMocks
    private ExampleValidationService validationService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @DisplayName("Should be successful on validateOnCreate() when example is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenExampleIsValid() {
        Example example = new Example();
        example.setText("This is an Example text");
        example.setId(null);

        assertDoesNotThrow(() -> validationService.validateOnCreate(example));
        verifyNoInteractions(persistenceService);
    }
    @DisplayName("Should throw exception on validateOnCreate() when text does not include Example")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenTextDoesNotIncludeExample() {
        Example example = new Example();
        example.setText("Invalid text");
        example.setId(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnCreate(example));

        assertEquals("Text does need to include 'Example'", exception.getReason());
        assertEquals(ErrorKey.VALIDATION_DOES_NOT_INCLUDE, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        Example example = new Example();
        example.setText("This contains Example");
        example.setId(123L);

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnCreate(example));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }
}
