package ch.puzzle.pcts.service.scheduled;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.puzzletime.EmployeeAttributes;
import ch.puzzle.pcts.dto.puzzletime.EmployeeData;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MemberAttributesSyncJobTest {

    @Mock
    private MemberBusinessService memberBusinessService;

    @Mock
    private OrganisationUnitBusinessService organisationUnitBusinessService;

    private MemberAttributesSyncJob syncJob;

    @BeforeEach
    void setUp() {
        syncJob = new MemberAttributesSyncJob(memberBusinessService,
                                              organisationUnitBusinessService,
                                              true,
                                              "https://dummy-url.ch",
                                              "dummyUser",
                                              "dummyPass",
                                              "0 0 0 * 11 6");
    }

    @DisplayName("Should successfully update member when found by PTime ID")
    @Test
    void shouldSuccessfullyUpdateMember() throws PCTSException {
        EmployeeAttributes attributes = new EmployeeAttributes("Updated",
                                                               "Name",
                                                               "M1",
                                                               LocalDate.of(1999, 8, 10),
                                                               true,
                                                               "OrganisationUnit 1");
        EmployeeData apiEmployee = new EmployeeData(1L, "employee", attributes);

        Member clonedMember1 = cloneMember(MEMBER_1);
        OrganisationUnit clonedOrgUnit1 = cloneOrgUnit(ORG_UNIT_1);

        when(memberBusinessService.findByPtimeId(1L)).thenReturn(Optional.of(clonedMember1));
        when(organisationUnitBusinessService.findByName("OrganisationUnit 1")).thenReturn(Optional.of(clonedOrgUnit1));

        ReflectionTestUtils.invokeMethod(syncJob, "processAndSaveMembers", List.of(apiEmployee));

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
    void shouldSetMissingPtimeIdWhenMatchedByAbbreviation() throws PCTSException {
        EmployeeAttributes attributes = new EmployeeAttributes("Member 2",
                                                               "Test",
                                                               "M2",
                                                               null,
                                                               false,
                                                               "OrganisationUnit 2");
        EmployeeData apiEmployee = new EmployeeData(999L, "employee", attributes);

        Member clonedMember2 = cloneMember(MEMBER_2);
        clonedMember2.setPtimeId(null);
        OrganisationUnit clonedOrgUnit2 = cloneOrgUnit(ORG_UNIT_2);

        when(memberBusinessService.findByPtimeId(999L)).thenReturn(Optional.empty());
        when(memberBusinessService.findByAbbreviation("M2")).thenReturn(Optional.of(clonedMember2));
        when(organisationUnitBusinessService.findByName("OrganisationUnit 2")).thenReturn(Optional.of(clonedOrgUnit2));

        ReflectionTestUtils.invokeMethod(syncJob, "processAndSaveMembers", List.of(apiEmployee));

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberBusinessService).update(eq(clonedMember2.getId()), memberCaptor.capture());

        Member savedMember = memberCaptor.getValue();
        assertEquals(EmploymentState.EX_MEMBER, savedMember.getEmploymentState());

        verify(memberBusinessService)
                .updateSyncMetadata(eq(clonedMember2.getId()), eq(999L), any(LocalDateTime.class), eq(0));
    }

    @DisplayName("Should increment error count when mandatory name fields are missing")
    @Test
    void shouldIncrementErrorCountWhenNameIsMissing() throws PCTSException {
        EmployeeAttributes attributes = new EmployeeAttributes("", "Test", "M3", null, true, "OrganisationUnit 2");
        EmployeeData apiEmployee = new EmployeeData(3L, "employee", attributes);

        Member clonedMember3 = cloneMember(MEMBER_3);
        int initialErrorCount = clonedMember3.getSyncErrorCount() != null ? clonedMember3.getSyncErrorCount() : 0;

        when(memberBusinessService.findByPtimeId(3L)).thenReturn(Optional.of(clonedMember3));
        when(memberBusinessService.getById(clonedMember3.getId())).thenReturn(clonedMember3);

        ReflectionTestUtils.invokeMethod(syncJob, "processAndSaveMembers", List.of(apiEmployee));

        verify(memberBusinessService, never()).update(anyLong(), any(Member.class));

        verify(memberBusinessService)
                .updateSyncMetadata(eq(clonedMember3.getId()), isNull(), isNull(), eq(initialErrorCount + 1));

        verifyNoInteractions(organisationUnitBusinessService);
    }

    @DisplayName("Should ignore API record completely when user is not found in DB")
    @Test
    void shouldIgnoreRecordWhenUserIsNotMatched() throws PCTSException {
        EmployeeAttributes attributes = new EmployeeAttributes("Ghost",
                                                               "User",
                                                               "gho",
                                                               null,
                                                               true,
                                                               "OrganisationUnit 1");
        EmployeeData apiEmployee = new EmployeeData(999L, "employee", attributes);

        when(memberBusinessService.findByPtimeId(999L)).thenReturn(Optional.empty());
        when(memberBusinessService.findByAbbreviation("gho")).thenReturn(Optional.empty());

        ReflectionTestUtils.invokeMethod(syncJob, "processAndSaveMembers", List.of(apiEmployee));

        verify(memberBusinessService, never()).update(any(), any());
        verifyNoInteractions(organisationUnitBusinessService);
    }

    @DisplayName("Should create and assign a new OrganisationUnit if it does not exist")
    @Test
    void shouldCreateNewOrgUnitIfNotExists() throws PCTSException {
        EmployeeAttributes attributes = new EmployeeAttributes("Updated",
                                                               "Name",
                                                               "M1",
                                                               LocalDate.of(1999, 8, 10),
                                                               true,
                                                               "New Department");
        EmployeeData apiEmployee = new EmployeeData(1L, "employee", attributes);

        OrganisationUnit newlyCreatedOu = new OrganisationUnit();
        newlyCreatedOu.setId(99L);
        newlyCreatedOu.setName("New Department");

        Member clonedMember1 = cloneMember(MEMBER_1);

        when(memberBusinessService.findByPtimeId(1L)).thenReturn(Optional.of(clonedMember1));
        when(organisationUnitBusinessService.findByName("New Department")).thenReturn(Optional.empty());
        when(organisationUnitBusinessService.create(any(OrganisationUnit.class))).thenReturn(newlyCreatedOu);

        ReflectionTestUtils.invokeMethod(syncJob, "processAndSaveMembers", List.of(apiEmployee));

        ArgumentCaptor<OrganisationUnit> ouCaptor = ArgumentCaptor.forClass(OrganisationUnit.class);
        verify(organisationUnitBusinessService).create(ouCaptor.capture());
        assertEquals("New Department", ouCaptor.getValue().getName());

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberBusinessService).update(eq(clonedMember1.getId()), memberCaptor.capture());
        assertEquals(newlyCreatedOu, memberCaptor.getValue().getOrganisationUnit());
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
}
