package ch.puzzle.pcts.service.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    protected static Member createMember(EmploymentState employmentState, LocalDate birthDate, String firstName,
                                         String lastName, String abbreviation) {
        Member m = new Member();
        m.setEmploymentState(employmentState);
        m.setBirthDate(birthDate);
        m.setFirstName(firstName);
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
                            List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "firstName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "firstName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "  ", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "firstName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "S", "Test", "MT"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Member",
                                                    FieldKey.FIELD,
                                                    "firstName",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "  S ", "Test", "MT"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Member",
                                                    FieldKey.FIELD,
                                                    "firstName",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, tooLongString, "Test", "MT"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Member",
                                                    FieldKey.FIELD,
                                                    "firstName",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    tooLongString))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", null, "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "  ", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "S", "MT"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Member",
                                                    FieldKey.FIELD,
                                                    "lastName",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", "  S ", "MT"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Member",
                                                    FieldKey.FIELD,
                                                    "lastName",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, validPastDate, "Member", tooLongString, "MT"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Member",
                                                    FieldKey.FIELD,
                                                    "lastName",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    tooLongString))),
                    Arguments
                            .of(createMember(null, validPastDate, "Member", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "employmentState"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, null, "Member", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "birthDate"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, futureDate, "Member", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "birthDate"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, futureDate, "Member", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "birthDate"))));
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
