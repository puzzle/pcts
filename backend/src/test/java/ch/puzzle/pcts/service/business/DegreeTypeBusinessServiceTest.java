package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.dto.degree_type.DegreeTypeNameDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree_type.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeTypeValidationService;
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

class DegreeTypeBusinessServiceTest {

    @Mock
    private DegreeTypeValidationService validationService;

    @Mock
    private DegreeTypePersistenceService persistenceService;

    @InjectMocks
    private DegreeTypeBusinessService businessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should get degree type by id")
    @Test
    void shouldGetById() {
        DegreeType degreeType = new DegreeType(1L,
                                               "Degree type 1",
                                               new BigDecimal("1.0"),
                                               new BigDecimal("2.0"),
                                               new BigDecimal("3.0"));
        when(persistenceService.getById(1L)).thenReturn(Optional.of(degreeType));

        DegreeType result = businessService.getById(1L);

        assertEquals(degreeType, result);
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        when(persistenceService.getById(1L)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(1L));

        assertEquals("Degree type with id: " + 1 + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should get all Degree types")
    @Test
    void shouldGetAll() {
        List<DegreeType> degreeTypes = List
                .of(new DegreeType(1L,
                                   "Degree type 1",
                                   new BigDecimal("1.0"),
                                   new BigDecimal("2.0"),
                                   new BigDecimal("3.0")),
                    new DegreeType(1L,
                                   "Degree type 1",
                                   new BigDecimal("1.0"),
                                   new BigDecimal("2.0"),
                                   new BigDecimal("3.0")));
        when(persistenceService.getAll()).thenReturn(degreeTypes);

        List<DegreeType> result = businessService.getAll();

        assertArrayEquals(degreeTypes.toArray(), result.toArray());
        assertEquals(degreeTypes, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(new ArrayList<>());

        List<DegreeType> result = businessService.getAll();

        assertEquals(0, result.size());
    }

    @DisplayName("Should create degree type")
    @Test
    void shouldCreate() {
        DegreeType degreeType = new DegreeType(1L,
                                               "Degree type 1",
                                               new BigDecimal("1.0"),
                                               new BigDecimal("2.0"),
                                               new BigDecimal("3.0"));
        when(persistenceService.create(degreeType)).thenReturn(degreeType);

        DegreeType result = businessService.create(degreeType);

        assertEquals(degreeType, result);
        verify(validationService).validateOnCreate(degreeType);
        verify(persistenceService).create(degreeType);
    }

    @DisplayName("Should update degree type")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        DegreeType degreeType = new DegreeType(1L,
                                               "Degree type 1",
                                               new BigDecimal("1.0"),
                                               new BigDecimal("2.0"),
                                               new BigDecimal("3.0"));
        when(persistenceService.update(id, degreeType)).thenReturn(degreeType);

        DegreeType result = businessService.update(id, degreeType);

        assertEquals(degreeType, result);
        verify(validationService).validateOnUpdate(id, degreeType);
        verify(persistenceService).update(id, degreeType);
    }

    @DisplayName("Should delete role")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

    @DisplayName("Should get all names")
    @Test
    void shouldGetAllNames() {
        List<DegreeTypeNameDto> allNames = List
                .of(new DegreeTypeNameDto(1L, "Degree type 1"), new DegreeTypeNameDto(2L, "Degree type 2"));
        when(persistenceService.getAllNames()).thenReturn(allNames);

        List<DegreeTypeNameDto> result = businessService.getAllNames();

        assertEquals(allNames, result);
        verify(persistenceService).getAllNames();
    }
}
