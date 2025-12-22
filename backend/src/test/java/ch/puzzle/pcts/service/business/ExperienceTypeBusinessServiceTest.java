package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceTypeValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExperienceTypeBusinessServiceTest {

    @Mock
    private ExperienceTypeValidationService validationService;

    @Mock
    private ExperienceTypePersistenceService persistenceService;

    @Mock
    private ExperienceType experienceType;

    @Mock
    private List<ExperienceType> experienceTypes;

    @InjectMocks
    private ExperienceTypeBusinessService businessService;

    @DisplayName("Should get experienceType by id")
    @Test
    void shouldGetById() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(experienceType);

        ExperienceType result = businessService.getById(id);

        assertEquals(experienceType, result);
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
    }

    @DisplayName("Should get all experienceTypes")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(experienceTypes);
        when(experienceTypes.size()).thenReturn(2);

        List<ExperienceType> result = businessService.getAll();

        assertEquals(experienceTypes, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<ExperienceType> result = businessService.getAll();

        assertEquals(0, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should create experienceType")
    @Test
    void shouldCreate() {
        when(persistenceService.save(experienceType)).thenReturn(experienceType);

        ExperienceType result = businessService.create(experienceType);

        assertEquals(experienceType, result);
        verify(validationService).validateOnCreate(experienceType);
        verify(persistenceService).save(experienceType);
    }

    @DisplayName("Should update experienceType")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(experienceType)).thenReturn(experienceType);
        when(persistenceService.getById(id)).thenReturn(experienceType);

        ExperienceType result = businessService.update(id, experienceType);

        assertEquals(experienceType, result);
        verify(validationService).validateOnUpdate(id, experienceType);
        verify(experienceType).setId(id);
        verify(persistenceService).save(experienceType);
    }

    @DisplayName("Should delete experienceType")
    @Test
    void shouldDelete() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(experienceType);

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
