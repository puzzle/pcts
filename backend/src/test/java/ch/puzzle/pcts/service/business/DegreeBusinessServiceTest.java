package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeValidationService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DegreeBusinessServiceTest {

    @InjectMocks
    private DegreeBusinessService businessService;

    @Mock
    private DegreeValidationService validationService;

    @Mock
    private DegreePersistenceService persistenceService;

    @Mock
    private Degree degree;

    @DisplayName("Should get member by id")
    @Test
    void shouldGetById() {
        when(persistenceService.getById(1L)).thenReturn(Optional.of(degree));

        Degree result = businessService.getById(1L);

        assertEquals(degree, result);
        verify(persistenceService).getById(1L);
        verify(validationService).validateOnGetById(1L);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        when(persistenceService.getById(1L)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(1L));

        assertEquals("Degree with id: " + 1 + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(1L);
        verify(validationService).validateOnGetById(1L);
    }

    @DisplayName("Should create member")
    @Test
    void shouldCreate() {
        when(persistenceService.save(degree)).thenReturn(degree);

        Degree result = businessService.create(degree);

        assertEquals(degree, result);
        verify(validationService).validateOnCreate(degree);
        verify(persistenceService).save(degree);
    }

    @DisplayName("Should update member")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(degree)).thenReturn(degree);

        Degree result = businessService.update(id, degree);

        assertEquals(degree, result);
        verify(validationService).validateOnUpdate(id, degree);
        verify(degree).setId(id);
        verify(persistenceService).save(degree);
    }

    @DisplayName("Should delete member")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
