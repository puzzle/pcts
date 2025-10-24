package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeTypeValidationService;
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
class DegreeTypeBusinessServiceTest {

    @Mock
    private DegreeTypeValidationService validationService;

    @Mock
    private DegreeTypePersistenceService persistenceService;

    @Mock
    private DegreeType degreeType;

    @Mock
    private List<DegreeType> degreeTypes;

    @InjectMocks
    private DegreeTypeBusinessService businessService;

    @DisplayName("Should get degree type by id")
    @Test
    void shouldGetById() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(degreeType));

        DegreeType result = businessService.getById(id);

        assertEquals(degreeType, result);
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals("Degree type with id: " + 1 + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
    }

    @DisplayName("Should get all Degree types")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(degreeTypes);
        when(degreeTypes.size()).thenReturn(2);

        List<DegreeType> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(degreeTypes, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<DegreeType> result = businessService.getAll();

        assertEquals(0, result.size());
    }

    @DisplayName("Should create degree type")
    @Test
    void shouldCreate() {
        when(persistenceService.save(degreeType)).thenReturn(degreeType);

        DegreeType result = businessService.create(degreeType);

        assertEquals(degreeType, result);
        verify(validationService).validateOnCreate(degreeType);
        verify(persistenceService).save(degreeType);
    }

    @DisplayName("Should update degree type")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(degreeType)).thenReturn(degreeType);

        DegreeType result = businessService.update(id, degreeType);

        assertEquals(degreeType, result);
        verify(validationService).validateOnUpdate(id, degreeType);
        verify(degreeType).setId(id);
        verify(persistenceService).save(degreeType);
    }

    @DisplayName("Should delete role")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
