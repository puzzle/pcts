package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static ch.puzzle.pcts.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
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

class LeadershipExperienceCalculationValidationServiceTest
        extends
            ValidationBaseServiceTest<LeadershipExperienceCalculation, LeadershipExperienceCalculationValidationService> {

    @Override
    LeadershipExperienceCalculation getValidModel() {
        Member member = createMember(MEMBER_1_ID, "Alice", "Smith");
        Calculation calculation = createCalculation(1L, member);
        LeadershipExperience leadershipExperience = createLeadershipExperience(1L, member);

        return new LeadershipExperienceCalculation(null, calculation, leadershipExperience);
    }

    @Override
    LeadershipExperienceCalculationValidationService getService() {
        return new LeadershipExperienceCalculationValidationService();
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(new LeadershipExperienceCalculation(null, null, new LeadershipExperience()),
                            List
                                    .of(Map
                                            .of(FieldKey.CLASS,
                                                "LeadershipExperienceCalculation",
                                                FieldKey.FIELD,
                                                "calculation"))),
                    Arguments
                            .of(new LeadershipExperienceCalculation(null, new Calculation(), null),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "LeadershipExperienceCalculation",
                                                    FieldKey.FIELD,
                                                    "leadershipExperience"))));
    }

    @DisplayName("Should throw exception when members do not match")
    @Test
    void shouldThrowExceptionWhenMembersDoNotMatch() {
        LeadershipExperienceCalculationValidationService spyService = spy(getService());
        Member member2 = createMember(2L, "Bob", "Johnson");

        LeadershipExperienceCalculation lc = getValidModel();
        lc.getCalculation().setMember(member2);

        PCTSException exception = assertThrows(PCTSException.class, () -> spyService.validateMemberForCalculation(lc));

        assertEquals(ErrorKey.ATTRIBUTE_DOES_NOT_MATCH, exception.getErrorKeys().getFirst());
        assertEquals(Map
                .of(FieldKey.ENTITY,
                    CALCULATION,
                    FieldKey.FIELD,
                    "leadershipExperience",
                    FieldKey.CONDITION_FIELD,
                    "member"), exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should throw exception on duplicate leadershipExperience ID")
    @Test
    void shouldThrowExceptionOnDuplicateLeadershipExperienceId() {
        LeadershipExperienceCalculationValidationService spyService = spy(getService());
        LeadershipExperienceCalculation lc = getValidModel();
        LeadershipExperienceCalculation existingLc = getValidModel();
        existingLc.setId(1L);

        List<LeadershipExperienceCalculation> existing = List.of(existingLc);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> spyService.validateDuplicateLeadershipExperienceId(lc, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().getFirst());
        assertEquals(Map
                .of(FieldKey.ENTITY,
                    CALCULATION,
                    FieldKey.FIELD,
                    "leadershipExperience",
                    FieldKey.IS,
                    "LeadershipExperienceType 1"), exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should not throw exception when only same entity exists")
    @Test
    void shouldNotThrowWhenOnlySameEntityExists() {
        LeadershipExperienceCalculationValidationService spyService = spy(getService());
        LeadershipExperienceCalculation lc = getValidModel();
        lc.setId(1L);

        List<LeadershipExperienceCalculation> existing = List.of(lc);

        assertDoesNotThrow(() -> spyService.validateDuplicateLeadershipExperienceId(lc, existing));
    }

    private Member createMember(Long id, String firstName, String lastName) {
        Member member = new Member();
        member.setId(id);
        member.setEmploymentState(EmploymentState.APPLICANT);
        member.setBirthDate(LocalDate.of(1970, 1, 1));
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setAbbreviation("AA");
        member.setDateOfHire(LocalDate.EPOCH);
        member.setOrganisationUnit(OrganisationUnit.Builder.builder().withId(1L).withName("Organisation Unit").build());
        return member;
    }

    private Calculation createCalculation(Long id, Member member) {
        Calculation calculation = new Calculation();
        calculation.setId(id);
        calculation.setMember(member);
        return calculation;
    }

    private LeadershipExperience createLeadershipExperience(Long id, Member member) {
        LeadershipExperienceType leadershipExperienceType = new LeadershipExperienceType();
        leadershipExperienceType.setName("LeadershipExperienceType 1");

        LeadershipExperience leadershipExperience = new LeadershipExperience();
        leadershipExperience.setId(id);
        leadershipExperience.setMember(member);
        leadershipExperience.setLeadershipExperienceType(leadershipExperienceType);
        return leadershipExperience;
    }
}
