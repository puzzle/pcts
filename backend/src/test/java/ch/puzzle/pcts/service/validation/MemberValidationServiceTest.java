package ch.puzzle.pcts.service.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberValidationServiceTest extends ValidationBaseServiceTest<Member, MemberValidationService> {

    @Mock
    MemberPersistenceService persistenceService;

    @Spy
    @InjectMocks
    MemberValidationService service;

    @Override
    Member getValidModel() {
        return createMember(EmploymentState.MEMBER, LocalDate.EPOCH, "Member", "Test", "MT");
    }

    @Override
    MemberValidationService getService() {
        return service;
    }

    protected static Member createMember(EmploymentState employmentState, LocalDate birthDate, String name,
                                         String lastName, String abbreviation) {
        Member m = new Member();
        m.setEmploymentState(employmentState);
        m.setBirthDate(birthDate);
        m.setName(name);
        m.setLastName(lastName);
        m.setAbbreviation(abbreviation);
        m.setDateOfHire(LocalDate.EPOCH);
        m.setOrganisationUnit(new OrganisationUnit(1L, "Organisation Unit"));
        return m;
    }

    static Stream<Arguments> invalidModelProvider() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalDate validPastDate = LocalDate.of(1990, 1, 1);
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
                            .of(createMember(EmploymentState.MEMBER, null, "Member", "Test", "MT"),
                                "Member.birthDate must not be null."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, futureDate, "Member", "Test", "MT"),
                                "Member.birthDate must be in the past, given " + futureDate + "."),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, futureDate, "Member", "Test", "MT"),
                                "Member.birthDate must be in the past, given " + futureDate + "."));
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Member member = getValidModel();

        doNothing().when((ValidationBase<Member>) service).validateOnCreate(any());

        service.validateOnCreate(member);

        verify(service).validateOnCreate(member);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Member member = getValidModel();

        doNothing().when((ValidationBase<Member>) service).validateOnUpdate(anyLong(), any());

        service.validateOnUpdate(id, member);

        verify(service).validateOnUpdate(id, member);
        verifyNoMoreInteractions(persistenceService);
    }
}
