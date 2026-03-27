package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;
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
        return ORG_UNIT_2;
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

    @DisplayName("Should return orgUnit by name when found")
    @Test
    void shouldReturnMemberByName() {
        Optional<OrganisationUnit> result = persistenceService.findByName("OrganisationUnit 1");

        assertThat(result).isPresent().isEqualTo(Optional.of(ORG_UNIT_1));
    }

    @DisplayName("Should not return orgUnit by name when not found")
    @Test
    void shouldNotReturnMemberByName() {
        Optional<OrganisationUnit> result = persistenceService.findByName("Not a valid name");

        assertThat(result).isEmpty();
    }
}
