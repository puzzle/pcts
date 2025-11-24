package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.EXPERIENCE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceValidationService;
import java.util.Collections;
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
class ExperienceBusinessServiceTest {

    @Mock
    private ExperienceValidationService validationService;

    @Mock
    private ExperiencePersistenceService persistenceService;

    @Mock
    private Experience experience;

    @Mock
    private List<Experience> experiences;

    @InjectMocks
    private ExperienceBusinessService businessService;

    @DisplayName("Should get experience by id")
    @Test
    void shouldGetById() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(experience));

        Experience result = businessService.getById(id);

        assertEquals(experience, result);
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id", FieldKey.IS, id.toString(), FieldKey.ENTITY, EXPERIENCE)),
                     exception.getErrorAttributes());
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
    }

    @DisplayName("Should get all experiences")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(experiences);
        when(experiences.size()).thenReturn(2);

        List<Experience> result = businessService.getAll();

        assertEquals(experiences, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<Experience> result = businessService.getAll();

        assertEquals(0, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should create experience")
    @Test
    void shouldCreate() {
        when(persistenceService.save(experience)).thenReturn(experience);

        Experience result = businessService.create(experience);

        assertEquals(experience, result);
        verify(validationService).validateOnCreate(experience);
        verify(persistenceService).save(experience);
    }

    @DisplayName("Should update experience")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(experience)).thenReturn(experience);
        when(persistenceService.getById(id)).thenReturn(Optional.of(experience));

        Experience result = businessService.update(id, experience);

        assertEquals(experience, result);
        verify(validationService).validateOnUpdate(id, experience);
        verify(experience).setId(id);
        verify(persistenceService).save(experience);
    }

    @DisplayName("Should throw exception when updating non-existing experience")
    @Test
    void shouldThrowExceptionWhenUpdatingNotFound() {
        Long id = 1L;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(id, experience));

        verify(persistenceService).getById(id);
        verify(validationService, never()).validateOnUpdate(any(), any());
        verify(persistenceService, never()).save(any());
    }

    @DisplayName("Should delete experience")
    @Test
    void shouldDelete() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(experience));

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

    @DisplayName("Should throw exception when deleting non-existing experience")
    @Test
    void shouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.delete(id));

        verify(persistenceService).getById(id);
        verify(persistenceService, never()).delete(id);
    }
}
