package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeCalculationBusinessServiceTest
        extends
            BaseBusinessTest<DegreeCalculation, DegreeCalculationPersistenceService, DegreeCalculationValidationService, DegreeCalculationBusinessService> {

    private static final Long DEGREE_CALCULATION_ID_1 = 1L;

    private static final Long DEGREE_CALCULATION_ID_2 = 2L;

    private static final Long DEGREE_ID = 1L;

    @Mock
    private DegreeCalculationValidationService validationService;

    @Mock
    private DegreeCalculationPersistenceService persistenceService;

    @InjectMocks
    private DegreeCalculationBusinessService businessService;

    @Override
    DegreeCalculation getModel() {
        return mock(DegreeCalculation.class);
    }

    @Override
    DegreeCalculationPersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    DegreeCalculationValidationService getValidationService() {
        return validationService;
    }

    @Override
    DegreeCalculationBusinessService getBusinessService() {
        return businessService;
    }

    static Stream<Arguments> degreePointsProvider() {
        return Stream
                .of(Arguments
                        .of(Relevancy.HIGHLY, BigDecimal.valueOf(50), BigDecimal.valueOf(80), BigDecimal.valueOf(40)),
                    Arguments
                            .of(Relevancy.LIMITED,
                                BigDecimal.valueOf(30),
                                BigDecimal.valueOf(100),
                                BigDecimal.valueOf(30)),
                    Arguments
                            .of(Relevancy.LITTLE,
                                BigDecimal.valueOf(20),
                                BigDecimal.valueOf(50),
                                BigDecimal.valueOf(10)),
                    Arguments.of(Relevancy.HIGHLY, BigDecimal.ZERO, BigDecimal.valueOf(75), BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should get degree calculations by calculation id")
    void shouldGetByCalculationId() {
        DegreeCalculation dc = mock(DegreeCalculation.class);
        when(persistenceService.getByCalculationId(DEGREE_CALCULATION_ID_1)).thenReturn(List.of(dc));

        List<DegreeCalculation> result = businessService.getByCalculationId(DEGREE_CALCULATION_ID_1);

        assertEquals(1, result.size());
        verify(persistenceService).getByCalculationId(DEGREE_CALCULATION_ID_1);
    }

    @Test
    @DisplayName("Should get degree calculations by degree id")
    void shouldGetByDegreeId() {
        DegreeCalculation dc = mock(DegreeCalculation.class);
        when(persistenceService.getByDegreeId(DEGREE_CALCULATION_ID_1)).thenReturn(List.of(dc));

        List<DegreeCalculation> result = businessService.getByDegreeId(DEGREE_CALCULATION_ID_1);

        assertEquals(1, result.size());
        verify(persistenceService).getByDegreeId(DEGREE_CALCULATION_ID_1);
    }

    @ParameterizedTest
    @MethodSource("degreePointsProvider")
    @DisplayName("Should calculate degree points correctly")
    void shouldCalculateDegreePoints(Relevancy relevancy, BigDecimal basePoints, BigDecimal weight,
                                     BigDecimal expected) {

        DegreeType type = mock(DegreeType.class);
        when(type.getPointsByRelevancy(relevancy)).thenReturn(basePoints);

        Degree degree = mock(Degree.class);
        when(degree.getDegreeType()).thenReturn(type);

        DegreeCalculation dc = mock(DegreeCalculation.class);
        when(dc.getDegree()).thenReturn(degree);
        when(dc.getRelevancy()).thenReturn(relevancy);
        when(dc.getWeight()).thenReturn(weight);

        when(persistenceService.getByCalculationId(DEGREE_CALCULATION_ID_1)).thenReturn(List.of(dc));

        BigDecimal result = businessService.getDegreePoints(DEGREE_CALCULATION_ID_1);

        assertEquals(0, expected.compareTo(result));
        verify(persistenceService).getByCalculationId(DEGREE_CALCULATION_ID_1);
    }

    @Test
    @DisplayName("Should return zero points if calculation has no degrees")
    void shouldReturnZeroPoints() {
        when(persistenceService.getByCalculationId(DEGREE_CALCULATION_ID_1)).thenReturn(List.of());

        BigDecimal result = businessService.getDegreePoints(DEGREE_CALCULATION_ID_1);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @Override
    @DisplayName("Should create degree calculations for calculation")
    void shouldCreate() {
        Calculation calculation = mock(Calculation.class);

        Degree degree = mock(Degree.class);
        when(degree.getId()).thenReturn(DEGREE_CALCULATION_ID_1);

        DegreeCalculation dc = mock(DegreeCalculation.class);
        when(dc.getDegree()).thenReturn(degree);

        when(calculation.getDegrees()).thenReturn(List.of(dc));
        when(persistenceService.getByDegreeId(DEGREE_CALCULATION_ID_1)).thenReturn(List.of());
        when(persistenceService.save(dc)).thenReturn(dc);

        List<DegreeCalculation> result = businessService.createDegreeCalculations(calculation);

        assertEquals(1, result.size());
        verify(dc).setCalculation(calculation);
        verify(validationService).validateOnCreate(dc);
    }

    @Test
    @Override
    @DisplayName("Should update degree calculations and delete removed ones")
    void shouldUpdate() {
        Calculation calculation = mock(Calculation.class);
        when(calculation.getId()).thenReturn(DEGREE_CALCULATION_ID_1);

        Degree degree = mock(Degree.class);
        when(degree.getId()).thenReturn(DEGREE_ID);

        DegreeCalculation existing = new DegreeCalculation();
        existing.setId(DEGREE_CALCULATION_ID_2);

        DegreeCalculation updated = new DegreeCalculation();
        updated.setId(DEGREE_CALCULATION_ID_2);
        updated.setDegree(degree);

        when(calculation.getDegrees()).thenReturn(List.of(updated));
        when(persistenceService.getByCalculationId(DEGREE_CALCULATION_ID_1))
                .thenReturn(new ArrayList<>(List.of(existing)));
        when(persistenceService.save(any(DegreeCalculation.class))).thenAnswer(inv -> inv.getArgument(0));

        List<DegreeCalculation> result = businessService.updateDegreeCalculations(calculation);

        assertEquals(1, result.size());

        verify(validationService).validateOnUpdate(DEGREE_CALCULATION_ID_2, updated);

        verify(persistenceService).delete(anyLong());
    }

    @Test
    @DisplayName("Should return correct base points based on relevancy")
    void shouldReturnCorrectAmountOfPoints() {
        DegreeType degreeType = new DegreeType(null, null, BigDecimal.TEN, BigDecimal.TWO, BigDecimal.ONE);

        assertEquals(BigDecimal.TEN, degreeType.getPointsByRelevancy(Relevancy.HIGHLY));
        assertEquals(BigDecimal.TWO, degreeType.getPointsByRelevancy(Relevancy.LIMITED));
        assertEquals(BigDecimal.ONE, degreeType.getPointsByRelevancy(Relevancy.LITTLE));
    }
}
