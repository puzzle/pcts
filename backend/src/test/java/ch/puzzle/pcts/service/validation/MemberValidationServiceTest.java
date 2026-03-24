package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.MEMBER;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
        return createMember(EmploymentState.MEMBER,
                            LocalDate.EPOCH,
                            "Member",
                            "Test",
                            "MT",
                            1L,
                            LocalDateTime.of(2000, 6, 1, 3, 20),
                            0);
    }

    @Override
    MemberValidationService getService() {
        return service;
    }

    protected static Member createMember(EmploymentState employmentState, LocalDate birthDate, String firstName,
                                         String lastName, String abbreviation, Long ptimeId,
                                         LocalDateTime lastSuccessfulSync, Integer syncErrorCount) {
        Member m = new Member();
        m.setEmploymentState(employmentState);
        m.setBirthDate(birthDate);
        m.setFirstName(firstName);
        m.setLastName(lastName);
        m.setAbbreviation(abbreviation);
        m.setDateOfHire(LocalDate.EPOCH);
        m.setOrganisationUnit(ORG_UNIT_1);
        m.setPtimeId(ptimeId);
        m.setLastSuccessfulSync(lastSuccessfulSync);
        m.setSyncErrorCount(syncErrorCount);
        return m;
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(createMember(EmploymentState.MEMBER,
                                         DATE_YESTERDAY,
                                         null,
                                         "Test",
                                         "MT",
                                         1L,
                                         LocalDateTime.of(2000, 6, 1, 3, 20),
                                         0),
                            List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "firstName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "firstName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "  ",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "firstName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "S",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
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
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "  S ",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
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
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             TOO_LONG_STRING,
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
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
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "Member",
                                             null,
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "Member",
                                             "",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "Member",
                                             "  ",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastName"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "Member",
                                             "S",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
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
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "Member",
                                             "  S ",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
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
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "Member",
                                             TOO_LONG_STRING,
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
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
                            .of(createMember(null,
                                             DATE_YESTERDAY,
                                             "Member",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "employmentState"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             null,
                                             "Member",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "birthDate"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_TOMORROW,
                                             "Member",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "birthDate"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_TOMORROW,
                                             "Member",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "birthDate"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "Member",
                                             "Test",
                                             "MT",
                                             0L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "ptimeId"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "Member",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(30000, 11, 1, 1, 1),
                                             0),
                                List.of(Map.of(FieldKey.CLASS, "Member", FieldKey.FIELD, "lastSuccessfulSync"))),
                    Arguments
                            .of(createMember(EmploymentState.MEMBER,
                                             DATE_YESTERDAY,
                                             "Member",
                                             "Test",
                                             "MT",
                                             1L,
                                             LocalDateTime.of(2000, 6, 1, 3, 20),
                                             -1),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Member",
                                                    FieldKey.FIELD,
                                                    "syncErrorCount",
                                                    FieldKey.IS,
                                                    "-1")))

                );
    }

    @DisplayName("Should throw error when birthDate is not in past")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validationOperations")
    void shouldFailWhenBirthDateIsNotInPast(String opName, BiConsumer<MemberValidationService, Member> validator) {
        Member member = getValidModel();
        member.setBirthDate(LocalDate.now());

        PCTSException exception = assertThrows(PCTSException.class, () -> validator.accept(service, member));

        assertEquals(List.of(Map.of(FieldKey.FIELD, "birthDate", FieldKey.CLASS, "Member")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should pass validation when birthDate is before dateOfHire")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validationOperations")
    void shouldPassWhenBirthDateIsBeforeHireDate(String opName, BiConsumer<MemberValidationService, Member> validator) {
        Member member = getValidModel();
        member.setBirthDate(LocalDate.now().minusYears(20));
        member.setDateOfHire(LocalDate.now());

        assertDoesNotThrow(() -> validator.accept(service, member));
    }

    @DisplayName("Should pass validation when birthDate equals dateOfHire")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validationOperations")
    void shouldPassWhenCompletedAtEqualsValidUntil(String opName,
                                                   BiConsumer<MemberValidationService, Member> validator) {
        Member member = getValidModel();
        LocalDate date = LocalDate.now().minusDays(1);
        member.setBirthDate(date);
        member.setDateOfHire(date);

        assertDoesNotThrow(() -> validator.accept(service, member));
    }

    @DisplayName("Should pass validation when dateOfHire is null")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validationOperations")
    void shouldPassWhenValidUntilIsNull(String opName, BiConsumer<MemberValidationService, Member> validator) {
        Member member = getValidModel();
        member.setDateOfHire(null);

        assertDoesNotThrow(() -> validator.accept(service, member));
    }

    @DisplayName("Should pass when ptimeId is unique")
    @Test
    void shouldPassWhenPtimeIdIsUnique() {
        Member member = getValidModel();
        member.setPtimeId(1L);

        assertDoesNotThrow(() -> service.validateOnUpdate(MEMBER_1_ID, member));
    }

    @DisplayName("Should throw error when ptimeId is not unique on validateOnUpdate")
    @Test
    void shouldFailWhenPtimeIdIsNotUnique() {
        Member member = getValidModel();
        member.setPtimeId(1L);
        when(persistenceService.findByPtimeId(anyLong())).thenReturn(Optional.of(member));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(MEMBER_1_ID, member));

        assertEquals(List.of(Map.of(FieldKey.FIELD, "ptimeId", FieldKey.CLASS, "Member")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should call correct validate methods on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Member member = getValidModel();

        doNothing().when((ValidationBase<Member>) service).validateDateIsBefore(any(), any(), any(), any(), any());

        service.validateOnCreate(member);

        verify(service).validateOnCreate(member);
        verify(service)
                .validateDateIsBefore(MEMBER,
                                      "dateOfBirth",
                                      member.getBirthDate(),
                                      "dateOfHire",
                                      member.getDateOfHire());

        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate methods on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Member member = getValidModel();

        doNothing().when((ValidationBase<Member>) service).validateDateIsBefore(any(), any(), any(), any(), any());

        service.validateOnUpdate(MEMBER_1_ID, member);

        verify(service).validateOnUpdate(MEMBER_1_ID, member);
        verify(service)
                .validateDateIsBefore(MEMBER,
                                      "dateOfBirth",
                                      member.getBirthDate(),
                                      "dateOfHire",
                                      member.getDateOfHire());

        verify(persistenceService).findByPtimeId(member.getPtimeId());
        verifyNoMoreInteractions(persistenceService);
    }

    static Stream<Arguments> validationOperations() {
        return Stream
                .of(Arguments
                        .of("validateOnCreate",
                            (BiConsumer<MemberValidationService, Member>) MemberValidationService::validateOnCreate),

                    Arguments
                            .of("validateOnUpdate",
                                (BiConsumer<MemberValidationService, Member>) (svc, member) -> svc
                                        .validateOnUpdate(MEMBER_1_ID, member)));
    }
}
