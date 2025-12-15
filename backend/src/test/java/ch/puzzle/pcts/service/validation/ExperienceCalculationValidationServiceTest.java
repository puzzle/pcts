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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExperienceCalculationValidationServiceTest {

    @InjectMocks
    private ExperienceCalculationValidationService service;

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

    private ExperienceCalculation createExperienceCalculation(Long id, Member member) {
        Calculation calculation = createCalculation(id, member);
        Experience experience = createExperience(id, member, "Experience " + id);
        return new ExperienceCalculation(id, calculation, experience, Relevancy.HIGHLY, "Comment");
    }

    @DisplayName("Should throw exception when members do not match")
    @Test
    void shouldThrowExceptionWhenMembersDoNotMatch() {
        Member member1 = createMember(1L, "Alice", "Smith");
        Member member2 = createMember(2L, "Bob", "Johnson");

        ExperienceCalculation ec = createExperienceCalculation(1L, member1);
        ec.getCalculation().setMember(member2);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateMemberForCalculation(ec));
        assertEquals(ErrorKey.ATTRIBUTE_MATCHES, exception.getErrorKeys().get(0));
        assertEquals(Map
                .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "experience", FieldKey.CONDITION_FIELD, "member"),
                     exception.getErrorAttributes().get(0));
    }

    @DisplayName("Should throw exception on duplicate experience ID")
    @Test
    void shouldThrowExceptionOnDuplicateExperienceId() {
        Member member = createMember(1L, "Alice", "Smith");
        ExperienceCalculation ec = createExperienceCalculation(1L, member);

        List<ExperienceCalculation> existing = new ArrayList<>();
        existing.add(ec);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateDuplicateExperienceId(ec, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().get(0));
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "experience", FieldKey.IS, "Experience 1"),
                     exception.getErrorAttributes().get(0));
    }

    @DisplayName("Should return correct ID if experience calculation exists")
    @Test
    void shouldReturnIdByCalculationAndExperience() {
        Member member = createMember(1L, "Alice", "Smith");
        ExperienceCalculation ec1 = createExperienceCalculation(1L, member);
        ExperienceCalculation ec2 = createExperienceCalculation(2L, member);

        List<ExperienceCalculation> list = List.of(ec1, ec2);

        Long id = service.findIdByCalculationAndExperience(ec2, list);
        assertEquals(ec2.getId(), id);
    }

    @DisplayName("Should return null if experience calculation does not exist")
    @Test
    void shouldReturnNullIfNotFound() {
        Member member = createMember(1L, "Alice", "Smith");
        ExperienceCalculation ec1 = createExperienceCalculation(1L, member);
        ExperienceCalculation ec2 = createExperienceCalculation(2L, member);

        List<ExperienceCalculation> list = List.of(ec1);

        Long id = service.findIdByCalculationAndExperience(ec2, list);
        assertNull(id);
    }

    @DisplayName("Should call validateMemberForCalculation on validateOnCreate")
    @Test
    void shouldCallValidateMemberForCalculationOnCreate() {
        Member member = createMember(1L, "Alice", "Smith");

        Calculation calculation = createCalculation(1L, member);
        Experience experience = createExperience(1L, member, "Experience 1");
        ExperienceCalculation ec = new ExperienceCalculation(null,
                                                             calculation,
                                                             experience,
                                                             Relevancy.HIGHLY,
                                                             "Comment");

        ExperienceCalculationValidationService spyService = spy(service);
        doNothing().when(spyService).validateMemberForCalculation(any());

        spyService.validateOnCreate(ec);

        verify(spyService).validateMemberForCalculation(ec);
    }

}
