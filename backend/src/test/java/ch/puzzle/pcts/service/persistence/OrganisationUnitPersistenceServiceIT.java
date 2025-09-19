package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
import jakarta.transaction.Transactional;
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
class OrganisationUnitPersistenceServiceIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private OrganisationUnitPersistenceService persistenceService;

    @DisplayName("Should establish DB connection")
    @Test
    @Order(0)
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @DisplayName("Should get organisationUnit by id")
    @Test
    @Order(1)
    void shouldGetOrganisationUnitById() {
        Optional<OrganisationUnit> organisationUnit = persistenceService.getById(2L);

        assertThat(organisationUnit.isPresent()).isTrue();
        assertThat(organisationUnit.get().getId()).isEqualTo(2L);
    }

    @DisplayName("Should get all organisationUnits")
    @Test
    @Order(1)
    void shouldGetAllOrganisationUnits() {
        List<OrganisationUnit> all = persistenceService.getAll();

        assertThat(all).hasSize(1);
        assertThat(all).extracting(OrganisationUnit::getName).containsExactlyInAnyOrder("OrganisationUnit 2");
    }

    @DisplayName("Should create organisationUnit")
    @Transactional
    @Test
    @Order(2)
    void shouldCreate() {
        OrganisationUnit organisationUnit = new OrganisationUnit(null, "OrganisationUnit 3");

        OrganisationUnit result = persistenceService.create(organisationUnit);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(organisationUnit.getName());
    }

    @DisplayName("Should update organisationUnit")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long id = 2;
        OrganisationUnit organisationUnit = new OrganisationUnit(null, "Updated organisationUnit");

        persistenceService.update(id, organisationUnit);
        Optional<OrganisationUnit> result = persistenceService.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(organisationUnit.getName()).isEqualTo("Updated organisationUnit");
    }

    @DisplayName("Should delete organisationUnit")
    @Transactional
    @Test
    @Order(3)
    void shouldDelete() {
        long id = 2;

        persistenceService.delete(id);

        Optional<OrganisationUnit> result = persistenceService.getById(id);
        assertThat(result.isPresent()).isFalse();
    }
}
