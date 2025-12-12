package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceCalculationValidationService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    @DisplayName("Should get experience calculations by calculation id")
    @Test
    void shouldGetByCalculationId() {
        ExperienceCalculation exp = mock(ExperienceCalculation.class);
        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of(exp));

        List<ExperienceCalculation> result = businessService.getByCalculationId(ID);

        assertEquals(List.of(exp), result);
        verify(persistenceService).getByCalculationId(ID);
    }

    @DisplayName("Should get experience calculations by experience id")
    @Test
    void shouldGetByExperienceId() {
        ExperienceCalculation exp = mock(ExperienceCalculation.class);
        when(persistenceService.getByExperienceId(ID)).thenReturn(List.of(exp));

        List<ExperienceCalculation> result = businessService.getByExperienceId(ID);

        assertEquals(List.of(exp), result);
        verify(persistenceService).getByExperienceId(ID);
    }

    @DisplayName("Should calculate total experience points correctly")
    @Test
    void shouldCalculateExperiencePoints() {
        ExperienceType type = mock(ExperienceType.class);
        when(type.getHighlyRelevantPoints()).thenReturn(BigDecimal.valueOf(10));

        Experience exp = mock(Experience.class);
        when(exp.getType()).thenReturn(type);
        when(exp.getPercent()).thenReturn(50);
        when(exp.getStartDate()).thenReturn(LocalDate.now().minusYears(4));
        when(exp.getEndDate()).thenReturn(LocalDate.now());

        ExperienceCalculation calculation = mock(ExperienceCalculation.class);
        when(calculation.getExperience()).thenReturn(exp);
        when(calculation.getRelevancy()).thenReturn(Relevancy.HIGHLY);

        BigDecimal result = businessService.getExperiencePoints(List.of(calculation));

        assertEquals(0, result.compareTo(BigDecimal.valueOf(20)));
    }

    @DisplayName("Should calculate zero points if list is empty")
    @Test
    void shouldReturnZeroPointsOnEmptyList() {
        BigDecimal result = businessService.getExperiencePoints(List.of());
        assertEquals(BigDecimal.ZERO, result);
    }

    @DisplayName("Should use relevancy mapping correctly")
    @Test
    void shouldCalculateDifferentRelevancies() {
        ExperienceType type = mock(ExperienceType.class);
        when(type.getHighlyRelevantPoints()).thenReturn(BigDecimal.TEN);
        when(type.getLimitedRelevantPoints()).thenReturn(BigDecimal.valueOf(4));
        when(type.getLittleRelevantPoints()).thenReturn(BigDecimal.ONE);

        Experience exp = mock(Experience.class);
        when(exp.getType()).thenReturn(type);
        when(exp.getPercent()).thenReturn(100);
        when(exp.getStartDate()).thenReturn(LocalDate.now().minusYears(3));
        when(exp.getEndDate()).thenReturn(LocalDate.now());

        ExperienceCalculation high = mock(ExperienceCalculation.class);
        when(high.getExperience()).thenReturn(exp);
        when(high.getRelevancy()).thenReturn(Relevancy.HIGHLY);

        ExperienceCalculation limited = mock(ExperienceCalculation.class);
        when(limited.getExperience()).thenReturn(exp);
        when(limited.getRelevancy()).thenReturn(Relevancy.LIMITED);

        ExperienceCalculation little = mock(ExperienceCalculation.class);
        when(little.getExperience()).thenReturn(exp);
        when(little.getRelevancy()).thenReturn(Relevancy.LITTLE);

        BigDecimal result = businessService.getExperiencePoints(List.of(high, limited, little));

        assertEquals(BigDecimal.valueOf(45), result);
    }

    @DisplayName("Should validate and create experience calculation")
    @Test
    void shouldCreate() {
        ExperienceCalculation entity = mock(ExperienceCalculation.class);
        when(persistenceService.save(entity)).thenReturn(entity);

        ExperienceCalculation result = businessService.create(entity);

        assertEquals(entity, result);
        verify(validationService).validateOnCreate(entity);
        verify(persistenceService).save(entity);
    }

    @DisplayName("Should validate and update experience calculation")
    @Test
    void shouldUpdate() {
        ExperienceCalculation entity = mock(ExperienceCalculation.class);
        when(persistenceService.getById(ID)).thenReturn(java.util.Optional.of(entity));
        when(persistenceService.save(entity)).thenReturn(entity);

        ExperienceCalculation result = businessService.update(ID, entity);

        assertEquals(entity, result);
        verify(validationService).validateOnUpdate(ID, entity);
        verify(persistenceService).getById(ID);
        verify(persistenceService).save(entity);
    }

    @DisplayName("Should throw when updating non-existing entity")
    @Test
    void shouldThrowWhenUpdatingNotFound() {
        ExperienceCalculation entity = mock(ExperienceCalculation.class);
        when(persistenceService.getById(ID)).thenReturn(java.util.Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> businessService.update(ID, entity));

        verify(persistenceService).getById(ID);
        verify(persistenceService, never()).save(any());
    }
}
