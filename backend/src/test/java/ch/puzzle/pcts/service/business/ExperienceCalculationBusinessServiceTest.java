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
import java.util.Optional;
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

    private static final Long ID = 1L;

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
                        .of(Relevancy.HIGHLY,
                            BigDecimal.TEN,
                            50,
                            LocalDate.of(2020, 1, 1),
                            LocalDate.of(2024, 7, 1),
                            BigDecimal.valueOf(22.51)),
                    Arguments
                            .of(Relevancy.LIMITED,
                                BigDecimal.valueOf(5),
                                100,
                                LocalDate.of(2021, 1, 1),
                                LocalDate.of(2022, 1, 1),
                                BigDecimal.valueOf(5.00)),
                    Arguments
                            .of(Relevancy.LITTLE,
                                BigDecimal.valueOf(2),
                                25,
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 7, 1),
                                BigDecimal.valueOf(0.25)),
                    Arguments
                            .of(Relevancy.HIGHLY,
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
        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of(exp));

        List<ExperienceCalculation> result = businessService.getByCalculationId(ID);

        assertEquals(1, result.size());
        verify(persistenceService).getByCalculationId(ID);
    }

    @Test
    @DisplayName("Should get experience calculations by experience id")
    void shouldGetByExperienceId() {
        ExperienceCalculation exp = mock(ExperienceCalculation.class);
        when(persistenceService.getByExperienceId(ID)).thenReturn(List.of(exp));

        List<ExperienceCalculation> result = businessService.getByExperienceId(ID);

        assertEquals(1, result.size());
        verify(persistenceService).getByExperienceId(ID);
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

        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of(calculation));

        BigDecimal result = businessService.getExperiencePoints(ID);

        assertEquals(0, expected.compareTo(result.setScale(2, RoundingMode.HALF_UP)));
    }

    @Test
    @DisplayName("Should return zero points if calculation has no experiences")
    void shouldReturnZeroPoints() {
        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of());

        BigDecimal result = businessService.getExperiencePoints(ID);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @Override
    @DisplayName("Should create experience calculations for calculation")
    void shouldCreate() {
        Calculation calculation = mock(Calculation.class);
        ExperienceCalculation ec = mock(ExperienceCalculation.class);
        Experience exp = mock(Experience.class);

        when(calculation.getExperiences()).thenReturn(List.of(ec));
        when(ec.getExperience()).thenReturn(exp);
        when(exp.getId()).thenReturn(ID);
        when(persistenceService.getByExperienceId(any())).thenReturn(List.of());
        when(persistenceService.save(ec)).thenReturn(ec);

        List<ExperienceCalculation> result = businessService.createExperienceCalculations(calculation);

        assertEquals(1, result.size());
        verify(ec).setCalculation(calculation);
        verify(validationService).validateOnCreate(ec);
    }

    @Test
    @DisplayName("Should update experience calculations and delete removed ones")
    void shouldUpdateExperienceCalculations() {
        Long experienceCalculationId = 100L;

        Calculation calculation = mock(Calculation.class);
        when(calculation.getId()).thenReturn(ID);

        Experience experience = mock(Experience.class);

        ExperienceCalculation existing = new ExperienceCalculation();
        existing.setId(experienceCalculationId);

        ExperienceCalculation updated = new ExperienceCalculation();
        updated.setId(experienceCalculationId);
        updated.setExperience(experience);

        when(calculation.getExperiences()).thenReturn(List.of(updated));
        when(persistenceService.getByCalculationId(ID)).thenReturn(new ArrayList<>(List.of(existing)));

        when(persistenceService.getById(experienceCalculationId)).thenReturn(Optional.of(existing));

        when(persistenceService.save(any(ExperienceCalculation.class))).thenAnswer(inv -> inv.getArgument(0));

        List<ExperienceCalculation> result = businessService.updateExperienceCalculations(calculation);

        assertEquals(1, result.size());

        verify(validationService).validateOnUpdate(experienceCalculationId, updated);

        verify(persistenceService).delete(anyLong());
    }

    @Test
    @DisplayName("Should return correct base points based on relevancy")
    void shouldReceiveCorrectAmountOfPoints() {
        ExperienceType experienceType = new ExperienceType(null, null, BigDecimal.TEN, BigDecimal.TWO, BigDecimal.ONE);

        assertEquals(BigDecimal.TEN, experienceType.getPointsByRelevancy(Relevancy.HIGHLY));
        assertEquals(BigDecimal.TWO, experienceType.getPointsByRelevancy(Relevancy.LIMITED));
        assertEquals(BigDecimal.ONE, experienceType.getPointsByRelevancy(Relevancy.LITTLE));
    }
}