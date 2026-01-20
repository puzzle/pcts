package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

class DegreeCalculationValidationServiceTest
        extends
            ValidationBaseServiceTest<DegreeCalculation, DegreeCalculationValidationService> {

    @Override
    DegreeCalculationValidationService getService() {
        return new DegreeCalculationValidationService();
    }

    @Override
    DegreeCalculation getValidModel() {
        return new DegreeCalculation(null, CALCULATION_1, DEGREE_1, Relevancy.STRONGLY, VALID_WEIGHT, VALID_STRING);
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(new DegreeCalculation(null, null, DEGREE_1, Relevancy.STRONGLY, VALID_WEIGHT, VALID_STRING),
                            List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "calculation"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      CALCULATION_1,
                                                      null,
                                                      Relevancy.STRONGLY,
                                                      VALID_WEIGHT,
                                                      VALID_STRING),
                                List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "degree"))),
                    Arguments
                            .of(new DegreeCalculation(null, CALCULATION_1, DEGREE_1, null, VALID_WEIGHT, VALID_STRING),
                                List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "relevancy"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      CALCULATION_1,
                                                      DEGREE_1,
                                                      Relevancy.STRONGLY,
                                                      null,
                                                      VALID_STRING),
                                List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "weight"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      CALCULATION_1,
                                                      DEGREE_1,
                                                      Relevancy.STRONGLY,
                                                      NEGATIVE_BIG_DECIMAL,
                                                      VALID_STRING),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeCalculation",
                                                    FieldKey.FIELD,
                                                    "weight",
                                                    FieldKey.MAX,
                                                    MAX_WEIGHT,
                                                    FieldKey.MIN,
                                                    MIN_WEIGHT,
                                                    FieldKey.IS,
                                                    NEGATIVE_BIG_DECIMAL.toString()))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      CALCULATION_1,
                                                      DEGREE_1,
                                                      Relevancy.STRONGLY,
                                                      TOO_HIGH_WEIGHT,
                                                      VALID_STRING),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeCalculation",
                                                    FieldKey.FIELD,
                                                    "weight",
                                                    FieldKey.MAX,
                                                    MAX_WEIGHT,
                                                    FieldKey.MIN,
                                                    MIN_WEIGHT,
                                                    FieldKey.IS,
                                                    TOO_HIGH_WEIGHT.toString()))));
    }

    @DisplayName("Should throw exception when members do not match")
    @Test
    void shouldThrowExceptionWhenMembersDoNotMatch() {
        DegreeCalculationValidationService spyService = spy(getService());
        DegreeCalculation dc = getValidModel();

        Calculation calculationForMember2 = new Calculation();
        calculationForMember2.setMember(MEMBER_2);
        dc.setCalculation(calculationForMember2);

        PCTSException exception = assertThrows(PCTSException.class, () -> spyService.validateMemberForCalculation(dc));

        assertEquals(ErrorKey.ATTRIBUTE_DOES_NOT_MATCH, exception.getErrorKeys().getFirst());
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "degree", FieldKey.CONDITION_FIELD, "member"),
                     exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should throw exception on duplicate degree ID")
    @Test
    void shouldThrowExceptionOnDuplicateDegreeId() {
        DegreeCalculationValidationService spyService = spy(getService());
        DegreeCalculation dc = getValidModel();
        DegreeCalculation existingDc = getValidModel();
        existingDc.setId(DEGREE_CALC_1_ID);

        List<DegreeCalculation> existing = List.of(existingDc);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> spyService.validateDuplicateDegreeId(dc, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().getFirst());
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "degree", FieldKey.IS, DEGREE_1.getName()),
                     exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should not throw exception when only same entity exists")
    @Test
    void shouldNotThrowWhenOnlySameEntityExists() {
        DegreeCalculationValidationService spyService = spy(getService());
        DegreeCalculation dc = getValidModel();
        dc.setId(DEGREE_CALC_1.getId());

        List<DegreeCalculation> existing = List.of(dc);

        assertDoesNotThrow(() -> spyService.validateDuplicateDegreeId(dc, existing));
    }

    @DisplayName("Should call validateMemberForCalculation on validateOnCreate")
    @Test
    void shouldCallValidateMemberForCalculationOnCreate() {
        DegreeCalculationValidationService spyService = spy(getService());
        DegreeCalculation dc = getValidModel();

        doNothing().when(spyService).validateMemberForCalculation(dc);

        spyService.validateOnCreate(dc);

        verify(spyService).validateMemberForCalculation(dc);
    }
}