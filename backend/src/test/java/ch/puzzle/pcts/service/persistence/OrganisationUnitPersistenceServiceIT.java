package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.ORGANISATION_UNITS;
import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.OrganisationUnitRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrganisationUnitPersistenceServiceIT
        extends
            PersistenceBaseIT<OrganisationUnit, OrganisationUnitRepository, OrganisationUnitPersistenceService> {

    private final OrganisationUnitPersistenceService service;

    @Autowired
    OrganisationUnitPersistenceServiceIT(OrganisationUnitPersistenceService service) {
        super(service);
        this.service = service;
    }

    @Override
    OrganisationUnit getModel() {
        return new OrganisationUnit(null, "OrganisationUnit 3");
    }

    @Override
    List<OrganisationUnit> getAll() {
        return ORGANISATION_UNITS;
    }

    @DisplayName("Should get organisationUnit by name")
    @Test
    void shouldGetRoleByName() {
        Optional<OrganisationUnit> result = service.getByName("OrganisationUnit 2");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("OrganisationUnit 2");
    }
}
