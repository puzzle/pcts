package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class MemberBusinessServiceTest {

    @Mock
    private MemberValidationService validationService;

    @Mock
    private MemberPersistenceService persistenceService;

    @InjectMocks
    private MemberBusinessService businessService;

    private OrganisationUnit organisationUnit;
    private final Date commonDate = new Date(0L);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        organisationUnit = new OrganisationUnit(1L, "/bbt");
    }

    @Captor
    ArgumentCaptor<Member> memberCaptor;

    @DisplayName("Should get member by id")
    @Test
    void shouldGetById() {
        Member member = Member.Builder
                .builder()
                .withId(1L)
                .withName("Member1")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("M1")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withIsAdmin(false)
                .withOrganisationUnit(organisationUnit)
                .build();;
        when(persistenceService.getById(1L)).thenReturn(Optional.of(member));

        Member result = businessService.getById(1L);

        assertEquals(member, result);
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        when(persistenceService.getById(1L)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(1L));

        assertEquals("Member with id: " + 1 + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should get all members")
    @Test
    void shouldGetAll() {
        List<Member> members = List
                .of(Member.Builder
                        .builder()
                        .withId(1L)
                        .withName("Member1")
                        .withLastName("Test")
                        .withEmploymentState(EmploymentState.APPLICANT)
                        .withAbbreviation("M1")
                        .withDateOfHire(commonDate)
                        .withBirthDate(commonDate)
                        .withIsAdmin(false)
                        .withOrganisationUnit(organisationUnit)
                        .build(),
                    Member.Builder
                            .builder()
                            .withId(1L)
                            .withName("Member2")
                            .withLastName("Test")
                            .withEmploymentState(EmploymentState.APPLICANT)
                            .withAbbreviation("M2")
                            .withDateOfHire(commonDate)
                            .withBirthDate(commonDate)
                            .withIsAdmin(false)
                            .withOrganisationUnit(organisationUnit)
                            .build());
        when(persistenceService.getAll()).thenReturn(members);

        List<Member> result = businessService.getAll();

        assertArrayEquals(members.toArray(), result.toArray());
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(new ArrayList<>());

        List<Member> result = businessService.getAll();

        assertEquals(0, result.size());
    }

    @DisplayName("Should create member")
    @Test
    void shouldCreate() {
        Member member = Member.Builder
                .builder()
                .withId(1L)
                .withName("Member1")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("M1")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withIsAdmin(false)
                .withOrganisationUnit(organisationUnit)
                .build();
        when(persistenceService.create(member)).thenReturn(member);

        Member result = businessService.create(member);

        assertEquals(member, result);
        verify(validationService).validateOnCreate(member);
        verify(persistenceService).create(member);
    }

    @DisplayName("Should update member")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        Member member = Member.Builder
                .builder()
                .withId(1L)
                .withName("Member1")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("M1")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withIsAdmin(false)
                .withOrganisationUnit(organisationUnit)
                .build();
        when(persistenceService.update(id, member)).thenReturn(member);

        Member result = businessService.update(id, member);

        assertEquals(member, result);
        verify(validationService).validateOnUpdate(member);
        verify(persistenceService).update(id, member);
    }

    @DisplayName("Should delete member")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

    @DisplayName("Should trim role name")
    @Test
    void shouldTrimRoleName() {

        businessService
                .create(Member.Builder
                        .builder()
                        .withId(1L)
                        .withName("Member1")
                        .withLastName("Test")
                        .withEmploymentState(EmploymentState.APPLICANT)
                        .withAbbreviation("M1")
                        .withDateOfHire(commonDate)
                        .withBirthDate(commonDate)
                        .withIsAdmin(false)
                        .withOrganisationUnit(organisationUnit)
                        .build());

        verify(persistenceService).create(memberCaptor.capture());
        Member savedMember = memberCaptor.getValue();

        assertEquals("Member1", savedMember.getName());
        assertEquals(1L, savedMember.getId());
    }
}
