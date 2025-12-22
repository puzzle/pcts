package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceValidationService;
import java.util.List;
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
        when(persistenceService.getById(id)).thenReturn(experience);

        Experience result = businessService.getById(id);

        assertEquals(experience, result);
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
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
        when(persistenceService.getById(id)).thenReturn(experience);

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
        when(persistenceService.getById(id)).thenReturn(experience);

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
