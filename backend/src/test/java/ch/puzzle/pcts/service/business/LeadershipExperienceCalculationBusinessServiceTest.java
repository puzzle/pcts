package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.LeadershipExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceCalculationValidationService;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceCalculationBusinessServiceTest
        extends
            BaseBusinessTest<LeadershipExperienceCalculation, LeadershipExperienceCalculationPersistenceService, LeadershipExperienceCalculationValidationService, LeadershipExperienceCalculationBusinessService> {

    private static final Long CALCULATION_ID_1 = 1L;
    private static final Long LEADERSHIP_EXP_CALC_ID_1 = 1L;
    private static final Long LEADERSHIP_EXP_ID_1 = 1L;

    @Mock
    private LeadershipExperienceCalculationValidationService validationService;

    @Mock
    private LeadershipExperienceCalculationPersistenceService persistenceService;

    @InjectMocks
    private LeadershipExperienceCalculationBusinessService businessService;

    @Override
    LeadershipExperienceCalculation getModel() {
        return mock(LeadershipExperienceCalculation.class);
    }

    @Override
    LeadershipExperienceCalculationPersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    LeadershipExperienceCalculationValidationService getValidationService() {
        return validationService;
    }

    @Override
    LeadershipExperienceCalculationBusinessService getBusinessService() {
        return businessService;
    }

    static Stream<Arguments> leadershipExperiencePointsProvider() {
        return Stream
                .of(Arguments.of(true, BigDecimal.TEN, BigDecimal.TEN),
                    Arguments.of(true, BigDecimal.ONE, BigDecimal.ONE),
                    Arguments.of(false, BigDecimal.TEN, BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should get leadership experience calculations by calculation id")
    void shouldGetByCalculationId() {
        LeadershipExperienceCalculation lec = mock(LeadershipExperienceCalculation.class);
        when(persistenceService.getByCalculationId(CALCULATION_ID_1)).thenReturn(List.of(lec));

        List<LeadershipExperienceCalculation> result = businessService.getByCalculationId(CALCULATION_ID_1);

        assertEquals(1, result.size());
        verify(persistenceService).getByCalculationId(CALCULATION_ID_1);
    }

    @Test
    @DisplayName("Should get leadership experience calculations by leadership experience id")
    void shouldGetByLeadershipExperienceId() {
        LeadershipExperienceCalculation lec = mock(LeadershipExperienceCalculation.class);
        when(persistenceService.getByLeadershipExperienceId(LEADERSHIP_EXP_ID_1)).thenReturn(List.of(lec));

        List<LeadershipExperienceCalculation> result = businessService.getByLeadershipExperienceId(LEADERSHIP_EXP_ID_1);

        assertEquals(1, result.size());
        verify(persistenceService).getByLeadershipExperienceId(LEADERSHIP_EXP_ID_1);
    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("Should calculate leadership experience points correctly based on management role")
    @ParameterizedTest
    @MethodSource("leadershipExperiencePointsProvider")
    void shouldCalculateLeadershipExperiencePoints(Boolean isManagement, BigDecimal expPoints,
                                                   BigDecimal expectedResult) {
        Calculation calculation = mock(Calculation.class);
        Role role = mock(Role.class);

        when(role.getIsManagement()).thenReturn(isManagement);
        when(calculation.getRole()).thenReturn(role);

        LeadershipExperienceType type = mock(LeadershipExperienceType.class);
        when(type.getPoints()).thenReturn(expPoints);

        LeadershipExperience exp = mock(LeadershipExperience.class);
        when(exp.getLeadershipExperienceType()).thenReturn(type);

        LeadershipExperienceCalculation lec = mock(LeadershipExperienceCalculation.class);
        when(lec.getCalculation()).thenReturn(calculation);
        when(lec.getLeadershipExperience()).thenReturn(exp);

        when(persistenceService.getByCalculationId(CALCULATION_ID_1)).thenReturn(List.of(lec));

        BigDecimal result = businessService.getLeadershipExperiencePoints(CALCULATION_ID_1);

        assertEquals(0, expectedResult.compareTo(result));
        verify(persistenceService).getByCalculationId(CALCULATION_ID_1);
    }

    @Test
    @DisplayName("Should return zero points if calculation has no leadership experiences")
    void shouldReturnZeroPoints() {
        when(persistenceService.getByCalculationId(CALCULATION_ID_1)).thenReturn(List.of());

        BigDecimal result = businessService.getLeadershipExperiencePoints(CALCULATION_ID_1);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @Override
    @DisplayName("Should create leadership experience calculations for calculation")
    void shouldCreate() {
        Calculation calculation = mock(Calculation.class);
        LeadershipExperienceCalculation lec = mock(LeadershipExperienceCalculation.class);
        LeadershipExperience exp = mock(LeadershipExperience.class);

        when(lec.getLeadershipExperience()).thenReturn(exp);
        when(exp.getId()).thenReturn(LEADERSHIP_EXP_ID_1);
        when(calculation.getLeadershipExperienceCalculations()).thenReturn(List.of(lec));

        when(persistenceService.getByLeadershipExperienceId(LEADERSHIP_EXP_ID_1)).thenReturn(List.of());
        when(persistenceService.save(lec)).thenReturn(lec);

        List<LeadershipExperienceCalculation> result = businessService
                .createLeadershipExperienceCalculations(calculation);

        assertEquals(1, result.size());
        verify(lec).setCalculation(calculation);
        verify(validationService).validateOnCreate(lec);
        verify(validationService).validateDuplicateLeadershipExperienceId(lec, List.of());
    }

    @Test
    @Override
    @DisplayName("Should update leadership experience calculations and delete removed ones")
    void shouldUpdate() {
        Calculation calculation = mock(Calculation.class);
        when(calculation.getId()).thenReturn(CALCULATION_ID_1);

        LeadershipExperience exp = mock(LeadershipExperience.class);
        when(exp.getId()).thenReturn(LEADERSHIP_EXP_ID_1);

        LeadershipExperienceCalculation existing = new LeadershipExperienceCalculation();
        existing.setId(LEADERSHIP_EXP_CALC_ID_1);

        LeadershipExperienceCalculation updated = new LeadershipExperienceCalculation();
        updated.setId(LEADERSHIP_EXP_CALC_ID_1);
        updated.setLeadershipExperience(exp);

        when(calculation.getLeadershipExperienceCalculations()).thenReturn(List.of(updated));

        when(persistenceService.getByCalculationId(CALCULATION_ID_1)).thenReturn(new ArrayList<>(List.of(existing)));

        when(persistenceService.getByLeadershipExperienceId(LEADERSHIP_EXP_ID_1)).thenReturn(List.of(updated));

        when(persistenceService.save(any(LeadershipExperienceCalculation.class))).thenAnswer(inv -> inv.getArgument(0));

        List<LeadershipExperienceCalculation> result = businessService
                .updateLeadershipExperienceCalculations(calculation);

        assertEquals(1, result.size());

        verify(validationService).validateOnUpdate(LEADERSHIP_EXP_CALC_ID_1, updated);
        verify(validationService).validateDuplicateLeadershipExperienceId(updated, List.of(updated));
        verify(persistenceService).deleteAllByIdInBatch(anyList());
    }
}