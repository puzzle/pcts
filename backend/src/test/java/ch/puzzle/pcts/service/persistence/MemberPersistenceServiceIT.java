package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberPersistenceServiceIT extends PersistenceBaseIT<Member, MemberRepository, MemberPersistenceService> {

    @Autowired
    MemberPersistenceServiceIT(MemberPersistenceService service) {
        super(service);
    }

    @Override
    Member getModel() {
        return Member.Builder
                .builder()
                .withId(null)
                .withFirstName("Member 3")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("M1")
                .withDateOfHire(LocalDate.of(2019, 8, 4))
                .withBirthDate(LocalDate.of(1970, 1, 1))
                .withOrganisationUnit(new OrganisationUnit(2L, "OrganisationUnit 2"))
                .withEmail("member3@puzzle.ch")
                .build();
    }

    @Override
    List<Member> getAll() {
        OrganisationUnit deletedOrganisationUnit = new OrganisationUnit(1L, "OrganisationUnit 1");
        deletedOrganisationUnit.setDeletedAt(LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));

        return List
                .of(Member.Builder
                        .builder()
                        .withId(1L)
                        .withFirstName("Member 1")
                        .withLastName("Test")
                        .withEmploymentState(EmploymentState.MEMBER)
                        .withAbbreviation("M1")
                        .withDateOfHire(LocalDate.of(2021, 7, 15))
                        .withBirthDate(LocalDate.of(1999, 8, 10))
                        .withOrganisationUnit(deletedOrganisationUnit)
                        .withEmail(null)
                        .build(),
                    Member.Builder
                            .builder()
                            .withId(2L)
                            .withFirstName("Member 2")
                            .withLastName("Test")
                            .withEmploymentState(EmploymentState.MEMBER)
                            .withAbbreviation("M2")
                            .withDateOfHire(LocalDate.of(2020, 6, 1))
                            .withBirthDate(LocalDate.of(1998, 3, 3))
                            .withOrganisationUnit(new OrganisationUnit(2L, "OrganisationUnit 2"))
                            .withEmail("member2@puzzle.ch")
                            .build());
    }

    @DisplayName("Should get member by email")
    @Transactional
    @Test
    void shouldGetMemberByEmail() {
        String email = "member2@puzzle.ch";

        Member result = persistenceService.getByEmail(email);

        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getFirstName()).isEqualTo("Member 2");
    }

    @DisplayName("Should throw exception when email does not exist")
    @Transactional
    @Test
    void shouldReturnEmptyWhenEmailDoesNotExistForGet() {
        assertThrows(PCTSException.class, () -> persistenceService.getByEmail("non-existent@puzzle.ch"));
    }

    @DisplayName("Should find member by email")
    @Transactional
    @Test
    void shouldFindMemberByEmail() {
        String email = "member2@puzzle.ch";

        Optional<Member> result = persistenceService.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getFirstName()).isEqualTo("Member 2");
    }

    @DisplayName("Should return empty when email does not exist")
    @Transactional
    @Test
    void shouldReturnEmptyWhenEmailDoesNotExistForFind() {
        Optional<Member> result = persistenceService.findByEmail("non-existent@puzzle.ch");

        assertThat(result).isNotPresent();
    }
}
