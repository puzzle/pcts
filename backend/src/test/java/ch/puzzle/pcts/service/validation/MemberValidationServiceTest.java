package ch.puzzle.pcts.service.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberValidationServiceTest extends ValidationBaseServiceTest<Member, MemberValidationService> {

    @InjectMocks
    MemberValidationService service;

    @Mock
    MemberPersistenceService persistenceService;

    @Override
    Member getModel() {
        return createMember(EmploymentState.MEMBER, Date.valueOf(LocalDate.of(1990, 1, 1)), "Member", "Test", "MT");
    }

    @Override
    MemberValidationService getService() {
        return service;
    }

    private static Member createMember(EmploymentState employmentState, java.util.Date birthDate, String name,
                                       String lastName, String abbreviation) {
        Member m = new Member();
        m.setEmploymentState(employmentState);
        m.setBirthDate(birthDate);
        m.setName(name);
        m.setLastName(lastName);
        m.setAbbreviation(abbreviation);
        m.setDateOfHire(new Timestamp(0L));
        m.setOrganisationUnit(new OrganisationUnit(1L, "Organisation Unit"));
        return m;
    }

    static Stream<Arguments> invalidModelProvider() {
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(1));
        Date validPastDate = Date.valueOf(LocalDate.of(1990, 1, 1));
        String tooLongString = new String(new char[251]).replace("\0", "s");

        return Stream
                .of(Arguments
                        .of(createMember(EmploymentState.MEMBER, validPastDate, null, "Test", "MT"),
                            "Member.firstName must not be null."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "", "Test", "MT"),
                                "Member.firstName must not be blank."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "  ", "Test", "MT"),
                                "Member.firstName must not be blank."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "S", "Test", "MT"),
                                "Member.firstName size must be between 2 and 250, given S."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "  S ", "Test", "MT"),
                                "Member.firstName size must be between 2 and 250, given S."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, tooLongString, "Test", "MT"),
                                String
                                        .format("Member.firstName size must be between 2 and 250, given %s.",
                                                tooLongString)),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", null, "MT"),
                                "Member.lastName must not be null."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "", "MT"),
                                "Member.lastName must not be blank."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "  ", "MT"),
                                "Member.lastName must not be blank."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "S", "MT"),
                                "Member.lastName size must be between 2 and 250, given S."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "  S ", "MT"),
                                "Member.lastName size must be between 2 and 250, given S."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", tooLongString, "MT"),
                                String
                                        .format("Member.lastName size must be between 2 and 250, given %s.",
                                                tooLongString)),
                    Arguments
                            .of(createMember(null, validPastDate, "Member", "Test", "MT"),
                                "Member.employmentState must not be null."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "Test", null),
                                "Member.abbreviation must not be null."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "Test", ""),
                                "Member.abbreviation must not be blank."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "Test", "  "),
                                "Member.abbreviation must not be blank."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "Test", "S"),
                                "Member.abbreviation size must be between 2 and 250, given S."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "Test", "  S "),
                                "Member.abbreviation size must be between 2 and 250, given S."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "Test", tooLongString),
                                String
                                        .format("Member.abbreviation size must be between 2 and 250, given %s.",
                                                tooLongString),
                                Arguments
                                        .of(createMember(EmploymentState.MEMBER, null, "Member", "Test", "MT"),
                                            "Member.birthDate must not be null."),
                                Arguments
                                        .of(createMember(EmploymentState.MEMBER, futureDate, "Member", "Test", "MT"),
                                            "Member.birthDate must be in the past, given " + futureDate + "."),
                                Arguments
                                        .of(createMember(EmploymentState.MEMBER, futureDate, "Member", "Test", "MT"),
                                            "Member.birthDate must be in the past, given " + futureDate + ".")));
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Member member = getModel();

        MemberValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Member>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(member);

        verify(spyService).validateOnCreate(member);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Member member = getModel();

        MemberValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Member>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, member);

        verify(spyService).validateOnUpdate(id, member);
        verifyNoMoreInteractions(persistenceService);
    }
}
