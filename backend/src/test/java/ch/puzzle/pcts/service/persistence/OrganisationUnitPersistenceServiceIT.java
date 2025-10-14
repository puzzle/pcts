package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrganisationUnitPersistenceServiceIT extends PersistenceBasicIT {

    @Autowired
    private OrganisationUnitPersistenceService persistenceService;

    @DisplayName("Should get organisationUnit by id")
    @Test
    void shouldGetOrganisationUnitById() {
        Optional<OrganisationUnit> organisationUnit = persistenceService.getById(2L);

        assertThat(organisationUnit).isPresent();
        assertThat(organisationUnit.get().getId()).isEqualTo(2L);
    }

    @DisplayName("Should get all organisationUnits")
    @Test
    void shouldGetAllOrganisationUnits() {
        List<OrganisationUnit> all = persistenceService.getAll();

        assertThat(all).hasSize(1);
        assertThat(all).extracting(OrganisationUnit::getName).containsExactlyInAnyOrder("OrganisationUnit 2");
    }

    @DisplayName("Should create organisationUnit")
    @Transactional
    @Test
    void shouldCreate() {
        OrganisationUnit organisationUnit = new OrganisationUnit(null, "OrganisationUnit 3");

        OrganisationUnit result = persistenceService.create(organisationUnit);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(organisationUnit.getName());
    }

    @DisplayName("Should update organisationUnit")
    @Transactional
    @Test
    void shouldUpdate() {
        Long id = 2L;
        OrganisationUnit organisationUnit = new OrganisationUnit(null, "Updated organisationUnit");

        persistenceService.update(id, organisationUnit);
        Optional<OrganisationUnit> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(organisationUnit.getName()).isEqualTo("Updated organisationUnit");
    }

    @DisplayName("Should delete organisationUnit")
    @Transactional
    @Test
    void shouldDelete() {
        Long id = 2L;

        persistenceService.delete(id);

        Optional<OrganisationUnit> result = persistenceService.getById(id);
        assertThat(result).isNotPresent();
    }

    @DisplayName("Should get organisationUnit by name")
    @Test
    void shouldGetRoleByName() {
        Optional<OrganisationUnit> result = persistenceService.getByName("OrganisationUnit 2");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("OrganisationUnit 2");
    }
}
