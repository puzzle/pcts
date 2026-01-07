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

    private static final Long MEMBER_ID_1 = 1L;
    private static final Long MEMBER_ID_2 = 2L;
    private static final Long CALCULATION_ID = 1L;
    private static final Long EXPERIENCE_ID = 1L;
    private static final Long ORGANISATION_UNIT_ID = 1L;

    private static final String FIRST_NAME_1 = "Alice";
    private static final String LAST_NAME_1 = "Smith";
    private static final String FIRST_NAME_2 = "Bob";
    private static final String LAST_NAME_2 = "Johnson";

    private static final String EXPERIENCE_NAME = "Experience 1";
    private static final String VALID_COMMENT = "Valid comment";
    private static final String COMMENT = "Comment";
    private static final String ABBREVIATION = "AA";
    private static final String ORG_UNIT_NAME = "Organisation Unit";

    private static final LocalDate BIRTH_DATE = LocalDate.of(1970, 1, 1);

    @Override
    ExperienceCalculationValidationService getService() {
        return new ExperienceCalculationValidationService();
    }

    @Override
    ExperienceCalculation getValidModel() {
        Member member = createMember(MEMBER_ID_1, FIRST_NAME_1, LAST_NAME_1);
        Calculation calculation = createCalculation(CALCULATION_ID, member);
        Experience experience = createExperience(EXPERIENCE_ID, member, EXPERIENCE_NAME);

        return new ExperienceCalculation(null, calculation, experience, Relevancy.HIGHLY, COMMENT);
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(new ExperienceCalculation(null, null, new Experience(), Relevancy.HIGHLY, VALID_COMMENT),
                            List.of(Map.of(FieldKey.CLASS, "ExperienceCalculation", FieldKey.FIELD, "calculation"))),
                    Arguments
                            .of(new ExperienceCalculation(null,
                                                          new Calculation(),
                                                          null,
                                                          Relevancy.HIGHLY,
                                                          VALID_COMMENT),
                                List.of(Map.of(FieldKey.CLASS, "ExperienceCalculation", FieldKey.FIELD, "experience"))),
                    Arguments
                            .of(new ExperienceCalculation(null,
                                                          new Calculation(),
                                                          new Experience(),
                                                          null,
                                                          VALID_COMMENT),
                                List.of(Map.of(FieldKey.CLASS, "ExperienceCalculation", FieldKey.FIELD, "relevancy"))));
    }

    @DisplayName("Should throw exception when members do not match")
    @Test
    void shouldThrowExceptionWhenMembersDoNotMatch() {
        ExperienceCalculationValidationService spyService = spy(getService());

        Member member2 = createMember(MEMBER_ID_2, FIRST_NAME_2, LAST_NAME_2);

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
        ec.getExperience().setName(EXPERIENCE_NAME);

        List<ExperienceCalculation> existing = List.of(ec);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> spyService.validateDuplicateExperienceId(ec, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().get(0));
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "experience", FieldKey.IS, EXPERIENCE_NAME),
                     exception.getErrorAttributes().get(0));
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

    private Member createMember(Long id, String firstName, String lastName) {
        Member member = new Member();
        member.setId(id);
        member.setEmploymentState(EmploymentState.APPLICANT);
        member.setBirthDate(BIRTH_DATE);
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setAbbreviation(ABBREVIATION);
        member.setDateOfHire(LocalDate.EPOCH);
        member.setOrganisationUnit(new OrganisationUnit(ORGANISATION_UNIT_ID, ORG_UNIT_NAME));
        return member;
    }

    private Calculation createCalculation(Long id, Member member) {
        Calculation calculation = new Calculation();
        calculation.setId(id);
        calculation.setMember(member);
        return calculation;
    }

    private Experience createExperience(Long id, Member member, String name) {
        Experience experience = new Experience();
        experience.setId(id);
        experience.setMember(member);
        experience.setName(name);
        return experience;
    }
}
