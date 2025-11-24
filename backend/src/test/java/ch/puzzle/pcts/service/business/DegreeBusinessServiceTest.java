package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.DEGREE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.service.persistence.DegreePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeValidationService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeBusinessServiceTest {

    @InjectMocks
    private DegreeBusinessService businessService;

    @Mock
    private DegreeValidationService validationService;

    @Mock
    private DegreePersistenceService persistenceService;

    @Mock
    private Degree degree;

    @DisplayName("Should get degree by id")
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
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id", FieldKey.IS, id.toString(), FieldKey.ENTITY, DEGREE)),
                     exception.getErrorAttributes());
        verify(persistenceService).getById(1L);
        verify(validationService).validateOnGetById(1L);
    }

    @DisplayName("Should create degree")
    @Test
    void shouldCreate() {
        when(persistenceService.save(degree)).thenReturn(degree);

        Degree result = businessService.create(degree);

        assertEquals(degree, result);
        verify(validationService).validateOnCreate(degree);
        verify(persistenceService).save(degree);
    }

    @DisplayName("Should update degree")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(degree)).thenReturn(degree);
        when(persistenceService.getById(id)).thenReturn(Optional.of(degree));

        Degree result = businessService.update(id, degree);

        assertEquals(degree, result);
        verify(validationService).validateOnUpdate(id, degree);
        verify(degree).setId(id);
        verify(persistenceService).save(degree);
    }

    @DisplayName("Should throw exception when updating non-existing degree")
    @Test
    void shouldThrowExceptionWhenUpdatingNotFound() {
        Long id = 1L;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(id, degree));

        verify(persistenceService).getById(id);
        verify(validationService, never()).validateOnUpdate(any(), any());
        verify(persistenceService, never()).save(any());
    }

    @DisplayName("Should delete degree")
    @Test
    void shouldDelete() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(degree));

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

    @DisplayName("Should throw exception when deleting non-existing degree")
    @Test
    void shouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.delete(id));

        verify(persistenceService).getById(id);
        verify(persistenceService, never()).delete(id);
    }
}
