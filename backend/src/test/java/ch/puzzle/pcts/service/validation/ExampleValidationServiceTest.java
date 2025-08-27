package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.persistence.ExamplePersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ExampleValidationServiceTest {

    @Mock
    private ExamplePersistenceService persistenceService;

    @InjectMocks
    private ExampleValidationService validationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateOnCreate_shouldPass_whenValidExample() {
        Example example = new Example();
        example.setText("This is an Example text");
        example.setId(null);

        assertDoesNotThrow(() -> validationService.validateOnCreate(example));
        verifyNoInteractions(persistenceService); // not used in current method
    }

    @Test
    void validateOnCreate_shouldThrow_whenTextDoesNotContainExample() {
        Example example = new Example();
        example.setText("Invalid text");
        example.setId(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnCreate(example));

        assertEquals("Text does need to include 'Example'", exception.getReason());
        assertEquals(ErrorKey.VALIDATION_DOES_NOT_INCLUDE, exception.getErrorKey());
    }

    @Test
    void validateOnCreate_shouldThrow_whenIdIsNotNull() {
        Example example = new Example();
        example.setText("This contains Example");
        example.setId(123L);

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnCreate(example));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }
}
