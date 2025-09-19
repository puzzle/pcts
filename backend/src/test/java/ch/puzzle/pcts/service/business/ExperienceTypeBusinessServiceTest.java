package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experienceType.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceTypeValidationService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ExperienceTypeBusinessServiceTest {

    @Mock
    private ExperienceTypeValidationService validationService;

    @Mock
    private ExperienceTypePersistenceService persistenceService;

    @InjectMocks
    private ExperienceTypeBusinessService businessService;

    private ExperienceType experienceType1;

    private ExperienceType experienceType2;

    @BeforeEach
    void setUp() {
        experienceType1 = new ExperienceType(1L,
                                             "ExperienceType 1",
                                             BigDecimal.valueOf(5.3),
                                             BigDecimal.valueOf(4.7),
                                             BigDecimal.valueOf(0));
        experienceType2 = new ExperienceType(2L,
                                             "ExperienceType 1",
                                             BigDecimal.valueOf(6),
                                             BigDecimal.valueOf(10),
                                             BigDecimal.valueOf(8));

        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should get experienceType by id")
    @Test
    void shouldGetById() {
        when(persistenceService.getById(1L)).thenReturn(Optional.of(experienceType1));

        ExperienceType result = businessService.getById(1L);

        assertEquals(experienceType1, result);
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        when(persistenceService.getById(1L)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(1L));

        assertEquals("ExperienceType with id: " + 1 + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should get all experienceTypes")
    @Test
    void shouldGetAll() {
        List<ExperienceType> experienceTypes = List.of(experienceType1, experienceType2);
        when(persistenceService.getAll()).thenReturn(experienceTypes);

        List<ExperienceType> result = businessService.getAll();

        assertArrayEquals(experienceTypes.toArray(), result.toArray());
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(new ArrayList<>());

        List<ExperienceType> result = businessService.getAll();

        assertEquals(0, result.size());
    }

    @DisplayName("Should create experienceType")
    @Test
    void shouldCreate() {
        when(persistenceService.create(experienceType1)).thenReturn(experienceType1);

        ExperienceType result = businessService.create(experienceType1);

        assertEquals(experienceType1, result);
        verify(validationService).validateOnCreate(experienceType1);
        verify(persistenceService).create(experienceType1);
    }

    @DisplayName("Should update experienceType")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.update(id, experienceType2)).thenReturn(experienceType2);

        ExperienceType result = businessService.update(id, experienceType2);

        assertEquals(experienceType2, result);
        verify(validationService).validateOnUpdate(id, experienceType2);
        verify(persistenceService).update(id, experienceType2);
    }

    @DisplayName("Should delete experienceType")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
