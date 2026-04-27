package ch.puzzle.pcts.service.scheduled;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import ch.puzzle.pcts.dto.puzzletime.EmployeeAttributes;
import ch.puzzle.pcts.dto.puzzletime.EmployeeData;
import ch.puzzle.pcts.dto.puzzletime.PuzzleTimeResponseDto;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
class MemberAttributesSyncJobTest {

    @Mock
    private MemberBusinessService memberBusinessService;

    @Mock
    private OrganisationUnitBusinessService organisationUnitBusinessService;

    private MemberAttributesSyncJob syncJob;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        // Further information:
        // https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-client.html
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();

        syncJob = new MemberAttributesSyncJob(memberBusinessService,
                                              organisationUnitBusinessService,
                                              builder,
                                              true,
                                              "https://dummy-url.ch",
                                              "dummyUser",
                                              "dummyPass",
                                              "0 0 0 * 11 6");
    }

    @DisplayName("Should successfully update member when found by PTime ID")
    @Test
    void shouldSuccessfullyUpdateMember() throws Exception {
        EmployeeAttributes attributes = new EmployeeAttributes("Updated",
                                                               "Name",
                                                               "M1",
                                                               LocalDate.of(1999, 8, 10),
                                                               true,
                                                               "OrganisationUnit 1");
        EmployeeData apiEmployee = new EmployeeData(1L, "employee", attributes);
        mockServerForPages(new PuzzleTimeResponseDto(List.of(apiEmployee)));

        Member clonedMember1 = cloneMember(MEMBER_1);
        OrganisationUnit clonedOrgUnit1 = cloneOrgUnit(ORG_UNIT_1);

        when(memberBusinessService.findByPtimeId(1L)).thenReturn(Optional.of(clonedMember1));
        when(organisationUnitBusinessService.findByName("OrganisationUnit 1")).thenReturn(Optional.of(clonedOrgUnit1));

        syncJob.syncMemberAttributes();

        mockServer.verify();
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberBusinessService).update(eq(clonedMember1.getId()), memberCaptor.capture());

        Member savedMember = memberCaptor.getValue();
        assertEquals("Updated", savedMember.getFirstName());
        assertEquals("Name", savedMember.getLastName());
        assertEquals(LocalDate.of(1999, 8, 10), savedMember.getBirthDate());
        assertEquals(EmploymentState.MEMBER, savedMember.getEmploymentState());
        assertEquals(clonedOrgUnit1, savedMember.getOrganisationUnit());
        assertEquals(0, savedMember.getSyncErrorCount());
        assertNotNull(savedMember.getLastSuccessfulSync());
    }

    @DisplayName("Should fallback to abbreviation, set PTime ID and update member")
    @Test
    void shouldSetMissingPtimeIdWhenMatchedByAbbreviation() throws Exception {
        EmployeeAttributes attributes = new EmployeeAttributes("Member 2",
                                                               "Test",
                                                               "M2",
                                                               null,
                                                               false,
                                                               "OrganisationUnit 2");
        EmployeeData apiEmployee = new EmployeeData(999L, "employee", attributes);
        mockServerForPages(new PuzzleTimeResponseDto(List.of(apiEmployee)));

        Member clonedMember2 = cloneMember(MEMBER_2);
        clonedMember2.setPtimeId(null);
        OrganisationUnit clonedOrgUnit2 = cloneOrgUnit(ORG_UNIT_2);

        when(memberBusinessService.findByPtimeId(999L)).thenReturn(Optional.empty());
        when(memberBusinessService.findByAbbreviation("M2")).thenReturn(Optional.of(clonedMember2));
        when(organisationUnitBusinessService.findByName("OrganisationUnit 2")).thenReturn(Optional.of(clonedOrgUnit2));

        syncJob.syncMemberAttributes();

        mockServer.verify();
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberBusinessService).update(eq(clonedMember2.getId()), memberCaptor.capture());

        Member savedMember = memberCaptor.getValue();
        assertEquals(EmploymentState.EX_MEMBER, savedMember.getEmploymentState());

        verify(memberBusinessService)
                .updateSyncMetadata(eq(clonedMember2.getId()), eq(999L), any(LocalDateTime.class), eq(0));
    }

    @DisplayName("Should increment error count when mandatory name fields are missing")
    @Test
    void shouldIncrementErrorCountWhenNameIsMissing() throws Exception {
        EmployeeAttributes attributes = new EmployeeAttributes("", "Test", "M3", null, true, "OrganisationUnit 2");
        EmployeeData apiEmployee = new EmployeeData(3L, "employee", attributes);
        mockServerForPages(new PuzzleTimeResponseDto(List.of(apiEmployee)));

        Member clonedMember3 = cloneMember(MEMBER_3);
        int initialErrorCount = clonedMember3.getSyncErrorCount() != null ? clonedMember3.getSyncErrorCount() : 0;

        when(memberBusinessService.findByPtimeId(3L)).thenReturn(Optional.of(clonedMember3));
        when(memberBusinessService.getById(clonedMember3.getId())).thenReturn(clonedMember3);

        syncJob.syncMemberAttributes();

        mockServer.verify();
        verify(memberBusinessService, never()).update(anyLong(), any(Member.class));
        verify(memberBusinessService)
                .updateSyncMetadata(eq(clonedMember3.getId()), isNull(), isNull(), eq(initialErrorCount + 1));
        verifyNoInteractions(organisationUnitBusinessService);
    }

    @DisplayName("Should ignore API record completely when user is not found in DB")
    @Test
    void shouldIgnoreRecordWhenUserIsNotMatched() throws Exception {
        EmployeeAttributes attributes = new EmployeeAttributes("Ghost",
                                                               "User",
                                                               "gho",
                                                               null,
                                                               true,
                                                               "OrganisationUnit 1");
        EmployeeData apiEmployee = new EmployeeData(999L, "employee", attributes);
        mockServerForPages(new PuzzleTimeResponseDto(List.of(apiEmployee)));

        when(memberBusinessService.findByPtimeId(999L)).thenReturn(Optional.empty());
        when(memberBusinessService.findByAbbreviation("gho")).thenReturn(Optional.empty());

        syncJob.syncMemberAttributes();

        mockServer.verify();
        verify(memberBusinessService, never()).update(any(), any());
        verifyNoInteractions(organisationUnitBusinessService);
    }

    @DisplayName("Should create and assign a new OrganisationUnit if it does not exist")
    @Test
    void shouldCreateNewOrgUnitIfNotExists() throws Exception {
        EmployeeAttributes attributes = new EmployeeAttributes("Updated",
                                                               "Name",
                                                               "M1",
                                                               LocalDate.of(1999, 8, 10),
                                                               true,
                                                               "New Department");
        EmployeeData apiEmployee = new EmployeeData(1L, "employee", attributes);
        mockServerForPages(new PuzzleTimeResponseDto(List.of(apiEmployee)));

        OrganisationUnit newlyCreatedOu = new OrganisationUnit();
        newlyCreatedOu.setId(99L);
        newlyCreatedOu.setName("New Department");

        Member clonedMember1 = cloneMember(MEMBER_1);

        when(memberBusinessService.findByPtimeId(1L)).thenReturn(Optional.of(clonedMember1));
        when(organisationUnitBusinessService.findByName("New Department")).thenReturn(Optional.empty());
        when(organisationUnitBusinessService.create(any(OrganisationUnit.class))).thenReturn(newlyCreatedOu);

        syncJob.syncMemberAttributes();

        mockServer.verify();
        ArgumentCaptor<OrganisationUnit> ouCaptor = ArgumentCaptor.forClass(OrganisationUnit.class);
        verify(organisationUnitBusinessService).create(ouCaptor.capture());
        assertEquals("New Department", ouCaptor.getValue().getName());

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberBusinessService).update(eq(clonedMember1.getId()), memberCaptor.capture());
        assertEquals(newlyCreatedOu, memberCaptor.getValue().getOrganisationUnit());
    }

    @DisplayName("Should not start job when used properties are invalid or not set at all")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("provideInvalidProperties")
    void shouldFailWithInvalidProperties(String testName, String apiUrl, String username, String password, String cron,
                                         String expectedErrorSnippet) {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            initalizeSyncJob(true, apiUrl, username, password, cron);
        });

        assertTrue(exception.getMessage().contains(expectedErrorSnippet),
                   "Expected error message containing: '" + expectedErrorSnippet + "' but got: '"
                                                                          + exception.getMessage() + "'");
    }

    @DisplayName("Should successfully skip validation when enabled is false")
    @Test
    void shouldNotFailWhenDisabled() {
        assertDoesNotThrow(() -> {
            initalizeSyncJob(false, null, "", "invalid-pass", "not-a-cron-string");
        }, "Expected no exception to be thrown because enabled is false");
    }

    @DisplayName("Should skip execution entirely when job is disabled")
    @Test
    void shouldSkipExecutionWhenDisabled() {
        MemberAttributesSyncJob disabledJob = initalizeSyncJob(false,
                                                               "https://dummy-url.ch",
                                                               "dummyUser",
                                                               "dummyPass",
                                                               "0 0 0 * 11 6");

        disabledJob.syncMemberAttributes();

        verifyNoInteractions(memberBusinessService);
        verifyNoInteractions(organisationUnitBusinessService);
    }

    @DisplayName("Should skip API record entirely when attributes are null")
    @Test
    void shouldSkipRecordWhenAttributesAreNull() throws Exception {
        EmployeeData apiEmployee = new EmployeeData(777L, "employee", null);

        mockServerForPages(new PuzzleTimeResponseDto(List.of(apiEmployee)));

        syncJob.syncMemberAttributes();

        mockServer.verify();

        verifyNoInteractions(memberBusinessService);
        verifyNoInteractions(organisationUnitBusinessService);
    }

    @DisplayName("Should increment error count when department name is null or blank")
    @ParameterizedTest(name = "{index} - Department name: ''{0}''")
    @MethodSource("provideInvalidDepartmentNames")
    void shouldIncrementErrorCountWhenDepartmentNameIsInvalid(String invalidDepartmentName) throws Exception {
        EmployeeAttributes attributes = new EmployeeAttributes("Valid",
                                                               "Name",
                                                               "val",
                                                               null,
                                                               true,
                                                               invalidDepartmentName);
        EmployeeData apiEmployee = new EmployeeData(4L, "employee", attributes);

        mockServer.reset();
        mockServerForPages(new PuzzleTimeResponseDto(List.of(apiEmployee)));

        Member clonedMember = cloneMember(MEMBER_1);
        int initialErrorCount = clonedMember.getSyncErrorCount() != null ? clonedMember.getSyncErrorCount() : 0;

        when(memberBusinessService.findByPtimeId(4L)).thenReturn(Optional.of(clonedMember));

        lenient().when(memberBusinessService.getById(clonedMember.getId())).thenReturn(clonedMember);

        syncJob.syncMemberAttributes();

        mockServer.verify();

        verify(memberBusinessService, never()).update(anyLong(), any(Member.class));

        verify(memberBusinessService)
                .updateSyncMetadata(eq(clonedMember.getId()), isNull(), isNull(), eq(initialErrorCount + 1));

        verifyNoInteractions(organisationUnitBusinessService);
    }

    private static Stream<Arguments> provideInvalidDepartmentNames() {
        return Stream.of(Arguments.of((String) null), Arguments.of(""), Arguments.of("   "));
    }

    // --- Helper Methods ---
    private void mockServerForPages(PuzzleTimeResponseDto page1Data) throws JsonProcessingException {
        mockServer
                .expect(requestTo("https://dummy-url.ch?page=1"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(page1Data), MediaType.APPLICATION_JSON));

        mockServer
                .expect(requestTo("https://dummy-url.ch?page=2"))
                .andRespond(withSuccess("{\"data\":[]}", MediaType.APPLICATION_JSON));
    }

    private MemberAttributesSyncJob initalizeSyncJob(Boolean enabled, String apiURL, String username, String password,
                                                     String cron) {
        return new MemberAttributesSyncJob(memberBusinessService,
                                           organisationUnitBusinessService,
                                           RestClient.builder(),
                                           enabled,
                                           apiURL,
                                           username,
                                           password,
                                           cron);
    }

    // Cloned objects because mocking the static ones causes problems
    private Member cloneMember(Member original) {
        if (original == null) {
            return null;
        }
        return Member.Builder
                .builder()
                .withId(original.getId())
                .withFirstName(original.getFirstName())
                .withLastName(original.getLastName())
                .withEmploymentState(original.getEmploymentState())
                .withAbbreviation(original.getAbbreviation())
                .withDateOfHire(original.getDateOfHire())
                .withBirthDate(original.getBirthDate())
                .withOrganisationUnit(cloneOrgUnit(original.getOrganisationUnit()))
                .withPtimeId(original.getPtimeId())
                .withLastSuccessfulSync(original.getLastSuccessfulSync())
                .withSyncErrorCount(0)
                .build();
    }

    private OrganisationUnit cloneOrgUnit(OrganisationUnit original) {
        if (original == null) {
            return null;
        }
        OrganisationUnit copy = OrganisationUnit.Builder
                .builder()
                .withId(original.getId())
                .withName(original.getName())
                .build();
        copy.setDeletedAt(original.getDeletedAt());
        return copy;
    }

    private static Stream<Arguments> provideInvalidProperties() {
        String validUrl = "https://example.com/api";
        String validUser = "admin";
        String validPass = "secret123";
        String validCron = "0 0 * * * *";

        return Stream
                .of(Arguments
                        .of("Null URL", null, validUser, validPass, validCron, "must start with http:// or https://"),
                    Arguments
                            .of("Blank URL",
                                "   ",
                                validUser,
                                validPass,
                                validCron,
                                "must start with http:// or https://"),
                    Arguments
                            .of("Invalid Scheme",
                                "ftp://example.com",
                                validUser,
                                validPass,
                                validCron,
                                "must start with http:// or https://"),
                    Arguments
                            .of("Missing Password",
                                validUrl,
                                validUser,
                                "",
                                validCron,
                                "must either both be provided or both be left blank"),
                    Arguments
                            .of("Missing Username",
                                validUrl,
                                null,
                                validPass,
                                validCron,
                                "must either both be provided or both be left blank"),
                    Arguments
                            .of("Null Cron",
                                validUrl,
                                validUser,
                                validPass,
                                null,
                                "must be a valid Spring cron expression"),
                    Arguments
                            .of("Blank Cron",
                                validUrl,
                                validUser,
                                validPass,
                                "   ",
                                "must be a valid Spring cron expression"),
                    Arguments
                            .of("Invalid Cron String",
                                validUrl,
                                validUser,
                                validPass,
                                "not-a-cron-string",
                                "must be a valid Spring cron expression"));
    }
}
