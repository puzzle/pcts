package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExperienceCalculationBusinessServiceTest {

    private static final Long ID = 1L;

    @Mock
    private ExperienceCalculationValidationService validationService;

    @Mock
    private ExperienceCalculationPersistenceService persistenceService;

    @InjectMocks
    private ExperienceCalculationBusinessService businessService;

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

    @Test
    @DisplayName("Should calculate experience points correctly")
    void shouldCalculateExperiencePoints() {
        ExperienceType type = mock(ExperienceType.class);
        when(type.getPointsByRelevancy(Relevancy.HIGHLY)).thenReturn(BigDecimal.TEN);

        Experience exp = mock(Experience.class);
        when(exp.getType()).thenReturn(type);
        when(exp.getPercent()).thenReturn(50);
        when(exp.getStartDate()).thenReturn(LocalDate.of(2020, 1, 1));
        when(exp.getEndDate()).thenReturn(LocalDate.of(2024, 7, 1));

        ExperienceCalculation calculation = mock(ExperienceCalculation.class);
        when(calculation.getExperience()).thenReturn(exp);
        when(calculation.getRelevancy()).thenReturn(Relevancy.HIGHLY);

        when(persistenceService.getByCalculationId(1L)).thenReturn(List.of(calculation));

        BigDecimal result = businessService.getExperiencePoints(1L);

        /*
         * basePoints = 10 (HIGHLY relevant) percent = 50% -> 0.5 years = 1643 days /
         * 365 = 4.5013698630 result = 10 * 0.5 * 4.5013698630 = 22.506849315
         */
        assertEquals(BigDecimal.valueOf(22.51), result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("Should return zero points for empty list")
    void shouldReturnZeroPoints() {
        BigDecimal result = businessService.getExperiencePoints(ID);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Should validate and create experience calculation")
    void shouldCreate() {
        ExperienceCalculation entity = mock(ExperienceCalculation.class);
        Experience experience = mock(Experience.class);

        when(entity.getExperience()).thenReturn(experience);
        when(experience.getId()).thenReturn(ID);
        when(persistenceService.getByExperienceId(ID)).thenReturn(List.of());
        when(persistenceService.save(entity)).thenReturn(entity);

        ExperienceCalculation result = businessService.create(entity);

        assertEquals(entity, result);
        verify(validationService).validateOnCreate(entity);
        verify(validationService).validateDuplicateExperienceId(eq(entity), anyList());
        verify(persistenceService).save(entity);
    }

    @Test
    @DisplayName("Should validate and update experience calculation")
    void shouldUpdate() {
        ExperienceCalculation entity = mock(ExperienceCalculation.class);

        when(persistenceService.getById(ID)).thenReturn(Optional.of(entity));
        when(persistenceService.save(entity)).thenReturn(entity);

        ExperienceCalculation result = businessService.update(ID, entity);

        assertEquals(entity, result);
        verify(validationService).validateOnUpdate(ID, entity);
        verify(validationService).validateMemberForCalculation(entity);
        verify(entity).setId(ID);
        verify(persistenceService).save(entity);
    }

    @Test
    @DisplayName("Should throw PCTSException when updating non-existing entity")
    void shouldThrowWhenUpdatingNotFound() {
        ExperienceCalculation entity = mock(ExperienceCalculation.class);
        when(persistenceService.getById(ID)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(ID, entity));

        verify(persistenceService).getById(ID);
        verify(persistenceService, never()).save(any());
    }

    @Test
    @DisplayName("Should create experience calculations for calculation")
    void shouldCreateExperienceCalculations() {
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
    @DisplayName("Should update, create and delete experience calculations correctly")
    void shouldUpdateExperienceCalculations() {
        Long experienceId = 10L;
        Long experienceCalculationId = 100L;

        Calculation calculation = mock(Calculation.class);
        when(calculation.getId()).thenReturn(ID);

        Experience experience = mock(Experience.class);
        when(experience.getId()).thenReturn(experienceId);

        ExperienceCalculation existing = mock(ExperienceCalculation.class);
        when(existing.getId()).thenReturn(experienceCalculationId);
        when(existing.getCalculation()).thenReturn(calculation);
        when(existing.getExperience()).thenReturn(experience);

        ExperienceCalculation updated = mock(ExperienceCalculation.class);
        when(updated.getExperience()).thenReturn(experience);
        when(updated.getCalculation()).thenReturn(calculation);

        when(calculation.getExperiences()).thenReturn(new ArrayList<>(List.of(updated)));

        when(persistenceService.getByCalculationId(ID)).thenReturn(new ArrayList<>(List.of(existing)));
        when(persistenceService.getById(experienceCalculationId)).thenReturn(Optional.of(existing));
        when(persistenceService.save(updated)).thenAnswer(inv -> inv.getArgument(0));

        List<ExperienceCalculation> result = businessService.updateExperienceCalculations(calculation);

        assertEquals(1, result.size());

        verify(updated).setCalculation(calculation);
        verify(updated).setId(experienceCalculationId);
        verify(validationService).validateOnUpdate(experienceCalculationId, updated);
    }

    @Test
    @DisplayName("Should receive correct amount of points")
    void shouldReceiveCorrectAmountOfPoints() {
        ExperienceType experienceType = new ExperienceType(null, null, BigDecimal.TEN, BigDecimal.TWO, BigDecimal.ONE);
        assertEquals(BigDecimal.TEN, experienceType.getPointsByRelevancy(Relevancy.HIGHLY));
        assertEquals(BigDecimal.TWO, experienceType.getPointsByRelevancy(Relevancy.LIMITED));
        assertEquals(BigDecimal.ONE, experienceType.getPointsByRelevancy(Relevancy.LITTLE));
    }
}
