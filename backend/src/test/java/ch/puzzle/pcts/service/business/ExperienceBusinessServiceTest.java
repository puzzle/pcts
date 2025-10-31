package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceValidationService;
import java.util.Collections;
import java.util.List;
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

        assertEquals(String.format("Experience with id: %d does not exist.", id), exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
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

        Experience result = businessService.update(id, experience);

        assertEquals(experience, result);
        verify(validationService).validateOnUpdate(id, experience);
        verify(experience).setId(id);
        verify(persistenceService).save(experience);
    }

    @DisplayName("Should delete experience")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
