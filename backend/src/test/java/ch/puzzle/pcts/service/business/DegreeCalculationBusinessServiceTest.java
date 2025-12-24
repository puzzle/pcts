package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
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
import java.util.Optional;
import java.util.stream.Stream;
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

    private static final Long ID = 1L;

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
                        .of(Relevancy.HIGHLY, BigDecimal.valueOf(50), BigDecimal.valueOf(85), BigDecimal.valueOf(42.5)),
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
    void shouldGetByCalculationId() {
        DegreeCalculation dc = mock(DegreeCalculation.class);
        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of(dc));

        List<DegreeCalculation> result = businessService.getByCalculationId(ID);

        assertEquals(1, result.size());
        verify(persistenceService).getByCalculationId(ID);
    }

    @Test
    void shouldGetByDegreeId() {
        DegreeCalculation dc = mock(DegreeCalculation.class);
        when(persistenceService.getByDegreeId(ID)).thenReturn(List.of(dc));

        List<DegreeCalculation> result = businessService.getByDegreeId(ID);

        assertEquals(1, result.size());
        verify(persistenceService).getByDegreeId(ID);
    }

    @ParameterizedTest
    @MethodSource("degreePointsProvider")
    void shouldCalculateDegreePoints(Relevancy relevancy, BigDecimal basePoints, BigDecimal weight,
                                     BigDecimal expected) {
        DegreeType type = mock(DegreeType.class);
        when(type.getPointsByRelevancy(relevancy)).thenReturn(basePoints);

        Degree degree = mock(Degree.class);
        when(degree.getDegreeType()).thenReturn(type);

        DegreeCalculation calculation = mock(DegreeCalculation.class);
        when(calculation.getDegree()).thenReturn(degree);
        when(calculation.getRelevancy()).thenReturn(relevancy);
        when(calculation.getWeight()).thenReturn(weight);

        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of(calculation));

        BigDecimal result = businessService.getDegreePoints(ID);

        assertEquals(0, expected.compareTo(result));
        verify(persistenceService).getByCalculationId(ID);
    }

    @Test
    void shouldReturnZeroPoints() {
        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of());

        BigDecimal result = businessService.getDegreePoints(ID);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @Override
    void shouldCreate() {
        Calculation calculation = mock(Calculation.class);
        DegreeCalculation dc = mock(DegreeCalculation.class);
        Degree degree = mock(Degree.class);

        when(calculation.getDegrees()).thenReturn(List.of(dc));
        when(dc.getDegree()).thenReturn(degree);
        when(degree.getId()).thenReturn(ID);
        when(persistenceService.getByDegreeId(ID)).thenReturn(List.of());
        when(persistenceService.save(dc)).thenReturn(dc);

        List<DegreeCalculation> result = businessService.createDegreeCalculations(calculation);

        assertEquals(1, result.size());
        verify(dc).setCalculation(calculation);
        verify(validationService).validateOnCreate(dc);
    }

    @Test
    void shouldUpdateDegreeCalculations() {
        Long degreeId = 10L;
        Long degreeCalculationId = 100L;

        Calculation calculation = mock(Calculation.class);
        when(calculation.getId()).thenReturn(ID);

        Degree degree = mock(Degree.class);
        when(degree.getId()).thenReturn(degreeId);

        DegreeCalculation existing = mock(DegreeCalculation.class);
        when(existing.getId()).thenReturn(degreeCalculationId);
        when(existing.getCalculation()).thenReturn(calculation);
        when(existing.getDegree()).thenReturn(degree);

        DegreeCalculation updated = mock(DegreeCalculation.class);
        when(updated.getDegree()).thenReturn(degree);
        when(updated.getCalculation()).thenReturn(calculation);

        when(calculation.getDegrees()).thenReturn(new ArrayList<>(List.of(updated)));
        when(persistenceService.getByCalculationId(ID)).thenReturn(new ArrayList<>(List.of(existing)));
        when(persistenceService.getById(degreeCalculationId)).thenReturn(Optional.of(existing));
        when(persistenceService.save(updated)).thenAnswer(inv -> inv.getArgument(0));

        List<DegreeCalculation> result = businessService.updateDegreeCalculations(calculation);

        assertEquals(1, result.size());
        verify(updated).setCalculation(calculation);
        verify(updated).setId(degreeCalculationId);
        verify(validationService).validateOnUpdate(degreeCalculationId, updated);
    }

    @Test
    void shouldReceiveCorrectAmountOfPoints() {
        DegreeType degreeType = new DegreeType(null, null, BigDecimal.TEN, BigDecimal.TWO, BigDecimal.ONE);
        assertEquals(BigDecimal.TEN, degreeType.getPointsByRelevancy(Relevancy.HIGHLY));
        assertEquals(BigDecimal.TWO, degreeType.getPointsByRelevancy(Relevancy.LIMITED));
        assertEquals(BigDecimal.ONE, degreeType.getPointsByRelevancy(Relevancy.LITTLE));
    }
}