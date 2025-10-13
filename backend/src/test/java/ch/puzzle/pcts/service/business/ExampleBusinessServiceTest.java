package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.persistence.ExamplePersistenceService;
import ch.puzzle.pcts.service.validation.ExampleValidationService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExampleBusinessServiceTest {

    @Mock
    private ExampleValidationService validationService;

    @Mock
    private ExamplePersistenceService persistenceService;

    @InjectMocks
    private ExampleBusinessService businessService;

    @DisplayName("Should get example by id")
    @Test
    void shouldGetById() {
        Example example = new Example(1L, "Example1");
        when(persistenceService.getById(1L)).thenReturn(example);

        Example result = businessService.getById(1L);

        assertEquals(example, result);
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should get all examples")
    @Test
    void shouldGetAll() {
        List<Example> examples = List.of(new Example(1L, "Example1"), new Example(2L, "Example2"));
        when(persistenceService.getAll()).thenReturn(examples);

        List<Example> result = businessService.getAll();

        assertArrayEquals(examples.toArray(), result.toArray());
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
    }

    @DisplayName("Should create example")
    @Test
    void shouldCreate() {
        Example example = new Example(1L, "Example1");
        when(persistenceService.create(example)).thenReturn(example);

        Example result = businessService.create(example);

        assertEquals(example, result);
        verify(validationService).validateOnCreate(example);
        verify(persistenceService).create(example);
    }
}
