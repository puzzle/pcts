package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.service.persistence.DegreeCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.DegreeCalculationValidationService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeCalculationBusinessServiceTest {

    private static final Long ID = 1L;

    @Mock
    private DegreeCalculationValidationService validationService;

    @Mock
    private DegreeCalculationPersistenceService persistenceService;

    @InjectMocks
    private DegreeCalculationBusinessService businessService;

    @Test
    @DisplayName("Should get degree calculations by calculation id")
    void shouldGetByCalculationId() {
        DegreeCalculation dc = mock(DegreeCalculation.class);
        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of(dc));

        List<DegreeCalculation> result = businessService.getByCalculationId(ID);

        assertEquals(1, result.size());
        verify(persistenceService).getByCalculationId(ID);
    }

    @Test
    @DisplayName("Should get degree calculations by degree id")
    void shouldGetByDegreeId() {
        DegreeCalculation dc = mock(DegreeCalculation.class);
        when(persistenceService.getByDegreeId(ID)).thenReturn(List.of(dc));

        List<DegreeCalculation> result = businessService.getByDegreeId(ID);

        assertEquals(1, result.size());
        verify(persistenceService).getByDegreeId(ID);
    }

    @Test
    @DisplayName("Should calculate degree points correctly")
    void shouldCalculateDegreePoints() {
        DegreeType type = mock(DegreeType.class);
        when(type.getPoints(Relevancy.HIGHLY)).thenReturn(BigDecimal.valueOf(50));

        Degree degree = mock(Degree.class);
        when(degree.getDegreeType()).thenReturn(type);

        DegreeCalculation calculation = mock(DegreeCalculation.class);
        when(calculation.getDegree()).thenReturn(degree);
        when(calculation.getRelevancy()).thenReturn(Relevancy.HIGHLY);
        when(calculation.getWeight()).thenReturn(BigDecimal.valueOf(85));

        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of(calculation));

        BigDecimal result = businessService.getDegreePoints(ID);

        /*
         * basePoints = 50 50 / 100 = 0.5 0.5 * 85 = 20
         */
        assertEquals(BigDecimal.valueOf(42.5), result);
    }

    @Test
    @DisplayName("Should return zero points for empty list")
    void shouldReturnZeroPoints() {
        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of());

        BigDecimal result = businessService.getDegreePoints(ID);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Should validate and create degree calculation")
    void shouldCreate() {
        DegreeCalculation entity = mock(DegreeCalculation.class);
        Degree degree = mock(Degree.class);

        when(entity.getDegree()).thenReturn(degree);
        when(degree.getId()).thenReturn(ID);
        when(persistenceService.getByDegreeId(ID)).thenReturn(List.of());
        when(persistenceService.save(entity)).thenReturn(entity);

        DegreeCalculation result = businessService.create(entity);

        assertEquals(entity, result);
        verify(validationService).validateOnCreate(entity);
        verify(validationService).validateDuplicateDegreeId(eq(entity), anyList());
        verify(persistenceService).save(entity);
    }

    @Test
    @DisplayName("Should validate and update degree calculation")
    void shouldUpdate() {
        DegreeCalculation entity = mock(DegreeCalculation.class);

        when(persistenceService.getById(ID)).thenReturn(Optional.of(entity));
        when(persistenceService.save(entity)).thenReturn(entity);

        DegreeCalculation result = businessService.update(ID, entity);

        assertEquals(entity, result);
        verify(validationService).validateOnUpdate(ID, entity);
        verify(validationService).validateMemberForCalculation(entity);
        verify(entity).setId(ID);
        verify(persistenceService).save(entity);
    }

    @Test
    @DisplayName("Should throw PCTSException when updating non-existing entity")
    void shouldThrowWhenUpdatingNotFound() {
        DegreeCalculation entity = mock(DegreeCalculation.class);
        when(persistenceService.getById(ID)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(ID, entity));

        verify(persistenceService).getById(ID);
        verify(persistenceService, never()).save(any());
    }

    @Test
    @DisplayName("Should create degree calculations for calculation")
    void shouldCreateDegreeCalculations() {
        Calculation calculation = mock(Calculation.class);
        DegreeCalculation dc = mock(DegreeCalculation.class);
        Degree degree = mock(Degree.class);

        when(calculation.getDegrees()).thenReturn(List.of(dc));
        when(dc.getDegree()).thenReturn(degree);
        when(degree.getId()).thenReturn(1L);
        when(persistenceService.getByDegreeId(any())).thenReturn(List.of());
        when(persistenceService.save(dc)).thenReturn(dc);

        List<DegreeCalculation> result = businessService.createDegreeCalculations(calculation);

        assertEquals(1, result.size());
        verify(dc).setCalculation(calculation);
        verify(validationService).validateOnCreate(dc);
    }

    @Test
    @DisplayName("Should update, create and delete degree calculations correctly")
    void shouldUpdateDegreeCalculations() {
        Calculation calculation = mock(Calculation.class);
        when(calculation.getId()).thenReturn(ID);

        Degree degree = mock(Degree.class);
        when(degree.getId()).thenReturn(10L);

        DegreeCalculation existing = mock(DegreeCalculation.class);
        when(existing.getId()).thenReturn(100L);
        when(existing.getCalculation()).thenReturn(calculation);
        when(existing.getDegree()).thenReturn(degree);

        DegreeCalculation updated = mock(DegreeCalculation.class);
        when(updated.getDegree()).thenReturn(degree);
        when(updated.getCalculation()).thenReturn(calculation);

        when(calculation.getDegrees()).thenReturn(new ArrayList<>(List.of(updated)));
        when(persistenceService.getByCalculationId(ID)).thenReturn(new ArrayList<>(List.of(existing)));
        when(persistenceService.getById(100L)).thenReturn(Optional.of(existing));
        when(persistenceService.save(any())).thenAnswer(inv -> inv.getArgument(0));

        List<DegreeCalculation> result = businessService.updateDegreeCalculations(calculation);

        assertEquals(1, result.size());
        verify(updated).setCalculation(calculation);
        verify(updated).setId(100L);
        verify(validationService).validateOnUpdate(100L, updated);
    }
}
