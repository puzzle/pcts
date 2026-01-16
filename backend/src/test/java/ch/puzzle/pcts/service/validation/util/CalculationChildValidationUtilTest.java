package ch.puzzle.pcts.service.validation.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationChild;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class CalculationChildValidationUtilTest {

    /*
     * This is needed because mockito can't mock multiple interfaces
     */
    private interface TestChild extends CalculationChild, Model {
    }

    @Mock
    private TestChild calculationChild1;
    @Mock
    private TestChild calculationChild2;
    @Mock
    private TestChild calculationChild3;

    @Mock
    private Calculation calculation1;
    @Mock
    private Calculation calculation2;
    @Mock
    private Calculation calculation3;

    @Test
    @DisplayName("Should return false when no matching calculation children exist")
    void shouldReturnFalseWhenNoMatches() {
        when(calculationChild1.getCalculation()).thenReturn(calculation1);
        when(calculationChild2.getCalculation()).thenReturn(calculation2);

        when(calculation1.getId()).thenReturn(100L);
        when(calculation2.getId()).thenReturn(200L);

        boolean result = CalculationChildValidationUtil.validateDuplicateCalculationChildId(
                calculationChild1, List.of(calculationChild2));

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when only match is the same entity")
    void shouldReturnFalseWhenOnlyMatchIsSameEntity() {
        when(calculationChild1.getId()).thenReturn(1L);
        when(calculationChild1.getCalculation()).thenReturn(calculation1);

        when(calculation1.getId()).thenReturn(100L);

        boolean result = CalculationChildValidationUtil.validateDuplicateCalculationChildId(
                calculationChild1, List.of(calculationChild1));

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when another entity has same calculation")
    void shouldReturnTrueWhenOtherEntityHasSameCalculation() {
        when(calculationChild1.getId()).thenReturn(1L);
        when(calculationChild2.getId()).thenReturn(2L);

        when(calculationChild1.getCalculation()).thenReturn(calculation1);
        when(calculationChild2.getCalculation()).thenReturn(calculation2);

        when(calculation1.getId()).thenReturn(100L);
        when(calculation2.getId()).thenReturn(100L);

        boolean result = CalculationChildValidationUtil.validateDuplicateCalculationChildId(
                calculationChild1, List.of(calculationChild2));

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return true when multiple matching entities exist")
    void shouldReturnTrueWhenMultipleMatches() {
        when(calculationChild1.getCalculation()).thenReturn(calculation1);
        when(calculationChild2.getCalculation()).thenReturn(calculation2);
        when(calculationChild3.getCalculation()).thenReturn(calculation3);

        when(calculation1.getId()).thenReturn(100L);
        when(calculation2.getId()).thenReturn(100L);
        when(calculation3.getId()).thenReturn(100L);

        boolean result = CalculationChildValidationUtil.validateDuplicateCalculationChildId(
                calculationChild1, List.of(calculationChild2, calculationChild3));

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when matching entity has same ID and calculation")
    void shouldReturnFalseWhenSameIDAndCalculation() {
        when(calculationChild1.getId()).thenReturn(5L);
        when(calculationChild2.getId()).thenReturn(5L);

        when(calculationChild1.getCalculation()).thenReturn(calculation1);
        when(calculationChild2.getCalculation()).thenReturn(calculation2);

        when(calculation1.getId()).thenReturn(100L);
        when(calculation2.getId()).thenReturn(100L);

        boolean result = CalculationChildValidationUtil.validateDuplicateCalculationChildId(
                calculationChild1, List.of(calculationChild2));

        assertFalse(result);
    }
}