package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
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
        m.setOrganisationUnit(ORG_UNIT_1);
        return m;
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, null, "Test", "MT"),
                            List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "firstName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "firstName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "  ", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "firstName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "S", "Test", "MT"),
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
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "  S ", "Test", "MT"),
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
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, TOO_LONG_STRING, "Test", "MT"),
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
                                                    TOO_LONG_STRING))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "Member", null, "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "Member", "", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "Member", "  ", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "Member", "S", "MT"),
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
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "Member", "  S ", "MT"),
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
                            .of(createMember(EmploymentState.MEMBER, DATE_YESTERDAY, "Member", TOO_LONG_STRING, "MT"),
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
                                                    TOO_LONG_STRING))),
                    Arguments
                            .of(createMember(null, DATE_YESTERDAY, "Member", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "employmentState"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, null, "Member", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "birthDate"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, DATE_TOMORROW, "Member", "Test", "MT"),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "birthDate"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER, DATE_TOMORROW, "Member", "Test", "MT"),
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
        Member member = getValidModel();

        doNothing().when((ValidationBase<Member>) service).validateOnUpdate(anyLong(), any());

        service.validateOnUpdate(MEMBER_1_ID, member);

        verify(service).validateOnUpdate(MEMBER_1_ID, member);
        verifyNoMoreInteractions(persistenceService);
    }
}
