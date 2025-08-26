package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.persistence.ExamplePersistenceService;
import ch.puzzle.pcts.service.validation.ExampleValidationService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ExampleBusinessServiceTest {

    @Mock
    private ExampleValidationService validationService;

    @Mock
    private ExamplePersistenceService persistenceService;

    @InjectMocks
    private ExampleBusinessService businessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Example example = new Example(1L, "Example1");
        when(persistenceService.getById(1L)).thenReturn(example);

        Example result = businessService.getById(1L);

        assertEquals(example, result);
        verify(persistenceService).getById(1L);
    }

    @Test
    void testGetAll() {
        List<Example> examples = List.of(new Example(1L, "Example1"), new Example(2L, "Example2"));
        when(persistenceService.getAll()).thenReturn(examples);

        List<Example> result = businessService.getAll();

        assertEquals(2, result.size());
        verify(persistenceService).getAll();
    }

    @Test
    void testCreate() {
        Example example = new Example(1L, "Example1");
        when(persistenceService.create(example)).thenReturn(example);

        Example result = businessService.create(example);

        assertEquals(example, result);
        verify(validationService).validateOnCreate(example);
        verify(persistenceService).create(example);
    }
}
