package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import jakarta.transaction.Transactional;
import java.util.Date;
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

    private final OrganisationUnit organisationUnit = new OrganisationUnit(1L, "/bbt");

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
    @Order(2)
    void shouldCreate() {
        Member member = new Member(null,
                                   "Member 3",
                                   "Test",
                                   EmploymentState.MEMBER,
                                   "M1",
                                   new Date(),
                                   new Date(),
                                   false,
                                   organisationUnit);

        Member result = persistenceService.create(member);

        assertThat(result.getId()).isEqualTo(1000L);
        assertThat(result.getName()).isEqualTo(member.getName());
    }

    @DisplayName("Should update members")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long id = 2;
        Member member = new Member(null,
                                   "Updated member",
                                   "Test",
                                   EmploymentState.MEMBER,
                                   "M1",
                                   new Date(),
                                   new Date(),
                                   false,
                                   organisationUnit);

        persistenceService.update(id, member);
        Optional<Member> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(member.getName()).isEqualTo("Updated member");
    }

    @DisplayName("Should delete members")
    @Transactional
    @Test
    @Order(3)
    void shouldDelete() {
        long id = 2;

        persistenceService.delete(id);

        Optional<Member> result = persistenceService.getById(id);
        assertThat(result).isNotPresent();
    }
}
