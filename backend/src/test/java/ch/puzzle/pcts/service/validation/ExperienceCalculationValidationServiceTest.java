package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.time.LocalDate;
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
        Member member = createMember(1L, "Alice", "Smith");
        Calculation calculation = createCalculation(1L, member);
        Experience experience = createExperience(1L, member, "Experience 1");

        return new ExperienceCalculation(null, calculation, experience, Relevancy.HIGHLY, "Comment");
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(new ExperienceCalculation(null, null, new Experience(), Relevancy.HIGHLY, "Valid comment"),
                            List.of(Map.of(FieldKey.CLASS, "ExperienceCalculation", FieldKey.FIELD, "calculation"))),

                    Arguments
                            .of(new ExperienceCalculation(null,
                                                          new Calculation(),
                                                          null,
                                                          Relevancy.HIGHLY,
                                                          "Valid comment"),
                                List.of(Map.of(FieldKey.CLASS, "ExperienceCalculation", FieldKey.FIELD, "experience"))),

                    Arguments
                            .of(new ExperienceCalculation(null,
                                                          new Calculation(),
                                                          new Experience(),
                                                          null,
                                                          "Valid comment"),
                                List.of(Map.of(FieldKey.CLASS, "ExperienceCalculation", FieldKey.FIELD, "relevancy"))));
    }

    @DisplayName("Should throw exception when members do not match")
    @Test
    void shouldThrowExceptionWhenMembersDoNotMatch() {
        ExperienceCalculationValidationService spyService = spy(getService());

        Member member2 = createMember(2L, "Bob", "Johnson");

        ExperienceCalculation ec = getValidModel();
        ec.getCalculation().setMember(member2);

        PCTSException exception = assertThrows(PCTSException.class, () -> spyService.validateMemberForCalculation(ec));

        assertEquals(ErrorKey.ATTRIBUTE_MATCHES, exception.getErrorKeys().get(0));
        assertEquals(Map
                .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "experience", FieldKey.CONDITION_FIELD, "member"),
                     exception.getErrorAttributes().get(0));
    }

    @DisplayName("Should throw exception on duplicate experience ID")
    @Test
    void shouldThrowExceptionOnDuplicateExperienceId() {
        ExperienceCalculationValidationService spyService = spy(getService());

        ExperienceCalculation ec = getValidModel();
        ec.getExperience().setName("Experience 1");

        List<ExperienceCalculation> existing = List.of(ec);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> spyService.validateDuplicateExperienceId(ec, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().get(0));
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "experience", FieldKey.IS, "Experience 1"),
                     exception.getErrorAttributes().get(0));
    }

    @DisplayName("Should call validateMemberForCalculation on validateOnCreate")
    @Test
    void shouldCallValidateMemberForCalculationOnCreate() {
        ExperienceCalculationValidationService spyService = spy(getService());
        ExperienceCalculation ec = getValidModel();

        doNothing().when(spyService).validateMemberForCalculation(any());

        spyService.validateOnCreate(ec);

        verify(spyService).validateMemberForCalculation(ec);
    }

    private Member createMember(Long id, String firstName, String lastName) {
        Member m = new Member();
        m.setId(id);
        m.setEmploymentState(EmploymentState.APPLICANT);
        m.setBirthDate(LocalDate.of(1970, 1, 1));
        m.setFirstName(firstName);
        m.setLastName(lastName);
        m.setAbbreviation("AA");
        m.setDateOfHire(LocalDate.EPOCH);
        m.setOrganisationUnit(new OrganisationUnit(1L, "Organisation Unit"));
        return m;
    }

    private Calculation createCalculation(Long id, Member member) {
        Calculation calc = new Calculation();
        calc.setId(id);
        calc.setMember(member);
        return calc;
    }

    private Experience createExperience(Long id, Member member, String name) {
        Experience exp = new Experience();
        exp.setId(id);
        exp.setMember(member);
        exp.setName(name);
        return exp;
    }
}
