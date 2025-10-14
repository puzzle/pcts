package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class MemberPersistenceServiceIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    private final OrganisationUnit organisationUnit = new OrganisationUnit(2L, "OrganisationUnit 2");

    private final Timestamp commonDate = new Timestamp(0L);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MemberPersistenceService persistenceService;

    @DisplayName("Should establish DB connection")
    @Test
    @Order(0)
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @DisplayName("Should get Member by id")
    @Test
    @Order(1)
    void shouldGetMemberById() {
        Optional<Member> optionalOrganisationUnit = persistenceService.getById(2L);

        assertThat(optionalOrganisationUnit).isPresent();
        assertThat(optionalOrganisationUnit.get().getId()).isEqualTo(2L);
    }

    @DisplayName("Should get all members")
    @Test
    @Order(1)
    void shouldGetAllMembers() {
        List<Member> all = persistenceService.getAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(Member::getName).containsExactlyInAnyOrder("Member 1", "Member 2");
    }

    @DisplayName("Should create members")
    @Transactional
    @Test
    @Order(3)
    void shouldCreate() {
        Member member = Member.Builder
                .builder()
                .withId(null)
                .withName("Member 3")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("M1")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();

        Member result = persistenceService.create(member);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getLastName()).isEqualTo(member.getLastName());
        assertThat(result.getEmploymentState()).isEqualTo(member.getEmploymentState());
        assertThat(result.getAbbreviation()).isEqualTo(member.getAbbreviation());
        assertThat(result.getDateOfHire()).isEqualTo(member.getDateOfHire());
        assertThat(result.getBirthDate()).isEqualTo(member.getBirthDate());
        assertThat(result.getOrganisationUnit().getId()).isEqualTo(member.getOrganisationUnit().getId());
        assertThat(result.getOrganisationUnit().getName()).isEqualTo(member.getOrganisationUnit().getName());
    }

    @DisplayName("Should update members")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        Long id = 2L;
        Member member = Member.Builder
                .builder()
                .withId(id)
                .withName("Updated member")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("M1")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();

        persistenceService.update(id, member);
        Optional<Member> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getName()).isEqualTo(member.getName());
        assertThat(result.get().getLastName()).isEqualTo(member.getLastName());
        assertThat(result.get().getEmploymentState()).isEqualTo(member.getEmploymentState());
        assertThat(result.get().getAbbreviation()).isEqualTo(member.getAbbreviation());
        assertThat(result.get().getDateOfHire()).isEqualTo(member.getDateOfHire());
        assertThat(result.get().getBirthDate()).isEqualTo(member.getBirthDate());
        assertThat(result.get().getOrganisationUnit().getId()).isEqualTo(member.getOrganisationUnit().getId());
        assertThat(result.get().getOrganisationUnit().getName()).isEqualTo(member.getOrganisationUnit().getName());
    }

    @DisplayName("Should delete members")
    @Transactional
    @Test
    @Order(3)
    void shouldDelete() {
        Long id = 2L;

        persistenceService.delete(id);

        Optional<Member> result = persistenceService.getById(id);
        assertThat(result).isNotPresent();
    }
}
