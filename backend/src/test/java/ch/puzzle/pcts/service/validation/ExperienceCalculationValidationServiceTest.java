package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static ch.puzzle.pcts.util.TestData.EXPERIENCE_CALC_1_ID;
import static ch.puzzle.pcts.util.TestData.VALID_STRING;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

class ExperienceCalculationValidationServiceTest
        extends
            ValidationBaseServiceTest<ExperienceCalculation, ExperienceCalculationValidationService> {

    @Override
    ExperienceCalculationValidationService getService() {
        return new ExperienceCalculationValidationService();
    }

    @Override
    ExperienceCalculation getValidModel() {
        return new ExperienceCalculation(null, CALCULATION_1, EXPERIENCE_1, Relevancy.STRONGLY, VALID_STRING);
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(new ExperienceCalculation(null, null, EXPERIENCE_1, Relevancy.STRONGLY, VALID_STRING),
                            List.of(Map.of(FieldKey.CLASS, "ExperienceCalculation", FieldKey.FIELD, "calculation"))),
                    Arguments
                            .of(new ExperienceCalculation(null, CALCULATION_1, null, Relevancy.STRONGLY, VALID_STRING),
                                List.of(Map.of(FieldKey.CLASS, "ExperienceCalculation", FieldKey.FIELD, "experience"))),
                    Arguments
                            .of(new ExperienceCalculation(null, CALCULATION_1, EXPERIENCE_1, null, VALID_STRING),
                                List.of(Map.of(FieldKey.CLASS, "ExperienceCalculation", FieldKey.FIELD, "relevancy"))));
    }

    @DisplayName("Should throw exception when members do not match")
    @Test
    void shouldThrowExceptionWhenMembersDoNotMatch() {
        ExperienceCalculationValidationService spyService = spy(getService());
        ExperienceCalculation ec = getValidModel();

        Calculation calculationForMember2 = new Calculation();
        calculationForMember2.setMember(MEMBER_2);
        ec.setCalculation(calculationForMember2);

        PCTSException exception = assertThrows(PCTSException.class, () -> spyService.validateMemberForCalculation(ec));

        assertEquals(ErrorKey.ATTRIBUTE_DOES_NOT_MATCH, exception.getErrorKeys().getFirst());
        assertEquals(Map
                .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "experience", FieldKey.CONDITION_FIELD, "member"),
                     exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should throw exception on duplicate experience ID")
    @Test
    void shouldThrowExceptionOnDuplicateExperienceId() {
        ExperienceCalculationValidationService spyService = spy(getService());
        ExperienceCalculation ec = getValidModel();
        ExperienceCalculation existingEc = getValidModel();
        existingEc.setId(EXPERIENCE_CALC_1_ID);
        List<ExperienceCalculation> existing = List.of(existingEc);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> spyService.validateDuplicateExperienceId(ec, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().getFirst());
        assertEquals(Map
                .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "experience", FieldKey.IS, EXPERIENCE_1.getName()),
                     exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should not throw exception when only same entity exists")
    @Test
    void shouldNotThrowWhenOnlySameEntityExists() {
        ExperienceCalculationValidationService spyService = spy(getService());
        ExperienceCalculation ec = getValidModel();
        ec.setId(EXP_CALC_1.getId());

        List<ExperienceCalculation> existing = List.of(ec);

        assertDoesNotThrow(() -> spyService.validateDuplicateExperienceId(ec, existing));
    }

    @DisplayName("Should call validateMemberForCalculation on validateOnCreate")
    @Test
    void shouldCallValidateMemberForCalculationOnCreate() {
        ExperienceCalculationValidationService spyService = spy(getService());
        ExperienceCalculation ec = getValidModel();

        doNothing().when(spyService).validateMemberForCalculation(ec);

        spyService.validateOnCreate(ec);

        verify(spyService).validateMemberForCalculation(ec);
    }
}