package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceCalculationValidationService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
class ExperienceCalculationBusinessServiceTest
        extends
            BaseBusinessTest<ExperienceCalculation, ExperienceCalculationPersistenceService, ExperienceCalculationValidationService, ExperienceCalculationBusinessService> {

    private static final Long EXPERIENCE_CALCULATION_ID_1 = 1L;
    private static final Long EXPERIENCE_CALCULATION_ID_2 = 2L;
    private static final Long EXPERIENCE_ID = 1L;

    @Mock
    private ExperienceCalculationValidationService validationService;

    @Mock
    private ExperienceCalculationPersistenceService persistenceService;

    @InjectMocks
    private ExperienceCalculationBusinessService businessService;

    @Override
    ExperienceCalculation getModel() {
        return mock(ExperienceCalculation.class);
    }

    @Override
    ExperienceCalculationPersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    ExperienceCalculationValidationService getValidationService() {
        return validationService;
    }

    @Override
    ExperienceCalculationBusinessService getBusinessService() {
        return businessService;
    }

    static Stream<Arguments> experiencePointsProvider() {
        return Stream
                .of(Arguments
                        .of(Relevancy.STRONGLY,
                            BigDecimal.TEN,
                            50,
                            LocalDate.of(2020, 1, 1),
                            LocalDate.of(2024, 7, 1),
                            BigDecimal.valueOf(22.51)),
                    Arguments
                            .of(Relevancy.NORMAL,
                                BigDecimal.valueOf(5),
                                100,
                                LocalDate.of(2021, 1, 1),
                                LocalDate.of(2022, 1, 1),
                                BigDecimal.valueOf(5.00)),
                    Arguments
                            .of(Relevancy.POORLY,
                                BigDecimal.valueOf(2),
                                25,
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 7, 1),
                                BigDecimal.valueOf(0.25)),
                    Arguments
                            .of(Relevancy.STRONGLY,
                                BigDecimal.TEN,
                                0,
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2024, 1, 1),
                                BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should get experience calculations by calculation id")
    void shouldGetByCalculationId() {
        ExperienceCalculation exp = mock(ExperienceCalculation.class);
        when(persistenceService.getByCalculationId(EXPERIENCE_CALCULATION_ID_1)).thenReturn(List.of(exp));

        List<ExperienceCalculation> result = businessService.getByCalculationId(EXPERIENCE_CALCULATION_ID_1);

        assertEquals(1, result.size());
        verify(persistenceService).getByCalculationId(EXPERIENCE_CALCULATION_ID_1);
    }

    @Test
    @DisplayName("Should get experience calculations by experience id")
    void shouldGetByExperienceId() {
        ExperienceCalculation exp = mock(ExperienceCalculation.class);
        when(persistenceService.getByExperienceId(EXPERIENCE_CALCULATION_ID_1)).thenReturn(List.of(exp));

        List<ExperienceCalculation> result = businessService.getByExperienceId(EXPERIENCE_CALCULATION_ID_1);

        assertEquals(1, result.size());
        verify(persistenceService).getByExperienceId(EXPERIENCE_CALCULATION_ID_1);
    }

    @ParameterizedTest
    @MethodSource("experiencePointsProvider")
    @DisplayName("Should calculate experience points correctly")
    void shouldCalculateExperiencePoints(Relevancy relevancy, BigDecimal basePoints, int percent, LocalDate startDate,
                                         LocalDate endDate, BigDecimal expected) {

        ExperienceType type = mock(ExperienceType.class);
        when(type.getPointsByRelevancy(relevancy)).thenReturn(basePoints);

        Experience exp = mock(Experience.class);
        when(exp.getType()).thenReturn(type);
        when(exp.getPercent()).thenReturn(percent);
        when(exp.getStartDate()).thenReturn(startDate);
        when(exp.getEndDate()).thenReturn(endDate);

        ExperienceCalculation calculation = mock(ExperienceCalculation.class);
        when(calculation.getExperience()).thenReturn(exp);
        when(calculation.getRelevancy()).thenReturn(relevancy);

        when(persistenceService.getByCalculationId(EXPERIENCE_CALCULATION_ID_1)).thenReturn(List.of(calculation));

        BigDecimal result = businessService.getExperiencePoints(EXPERIENCE_CALCULATION_ID_1);

        assertEquals(0, expected.compareTo(result.setScale(2, RoundingMode.HALF_UP)));
    }

    @Test
    @DisplayName("Should return zero points if calculation has no experiences")
    void shouldReturnZeroPoints() {
        when(persistenceService.getByCalculationId(EXPERIENCE_CALCULATION_ID_1)).thenReturn(List.of());

        BigDecimal result = businessService.getExperiencePoints(EXPERIENCE_CALCULATION_ID_1);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @Override
    @DisplayName("Should create experience calculations for calculation")
    void shouldCreate() {
        Calculation calculation = mock(Calculation.class);
        ExperienceCalculation ec = mock(ExperienceCalculation.class);
        Experience exp = mock(Experience.class);

        when(calculation.getExperienceCalculations()).thenReturn(List.of(ec));
        when(ec.getExperience()).thenReturn(exp);
        when(exp.getId()).thenReturn(EXPERIENCE_CALCULATION_ID_1);
        when(persistenceService.getByExperienceId(any())).thenReturn(List.of());
        when(persistenceService.save(ec)).thenReturn(ec);

        List<ExperienceCalculation> result = businessService.createExperienceCalculations(calculation);

        assertEquals(1, result.size());
        verify(ec).setCalculation(calculation);
        verify(validationService).validateOnCreate(ec);
    }

    @Test
    @Override
    @DisplayName("Should update experience calculations and delete removed ones")
    void shouldUpdate() {
        Calculation calculation = mock(Calculation.class);
        when(calculation.getId()).thenReturn(EXPERIENCE_CALCULATION_ID_1);

        Experience experience = mock(Experience.class);
        when(experience.getId()).thenReturn(EXPERIENCE_ID);

        ExperienceCalculation existing = new ExperienceCalculation();
        existing.setId(EXPERIENCE_CALCULATION_ID_2);

        ExperienceCalculation updated = new ExperienceCalculation();
        updated.setId(EXPERIENCE_CALCULATION_ID_2);
        updated.setExperience(experience);

        when(calculation.getExperienceCalculations()).thenReturn(List.of(updated));
        when(persistenceService.getByCalculationId(EXPERIENCE_CALCULATION_ID_1))
                .thenReturn(new ArrayList<>(List.of(existing)));
        when(persistenceService.save(any(ExperienceCalculation.class))).thenAnswer(inv -> inv.getArgument(0));

        List<ExperienceCalculation> result = businessService.updateExperienceCalculations(calculation);

        assertEquals(1, result.size());

        verify(validationService).validateOnUpdate(EXPERIENCE_CALCULATION_ID_2, updated);

        verify(persistenceService).deleteAllByIdInBatch(anyList());
    }

    @Test
    @DisplayName("Should return correct base points based on relevancy")
    void shouldReceiveCorrectAmountOfPoints() {
        ExperienceType experienceType = new ExperienceType(null, null, BigDecimal.TEN, BigDecimal.TWO, BigDecimal.ONE);

        assertEquals(BigDecimal.TEN, experienceType.getPointsByRelevancy(Relevancy.STRONGLY));
        assertEquals(BigDecimal.TWO, experienceType.getPointsByRelevancy(Relevancy.NORMAL));
        assertEquals(BigDecimal.ONE, experienceType.getPointsByRelevancy(Relevancy.POORLY));
    }
}