package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.ORGANISATION_UNIT;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.OrganisationUnitRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OrganisationUnitPersistenceService extends PersistenceBase<OrganisationUnit, OrganisationUnitRepository> {
    private final OrganisationUnitRepository organisationUnitRepository;

    public OrganisationUnitPersistenceService(OrganisationUnitRepository organisationUnitRepository) {
        super(organisationUnitRepository);
        this.organisationUnitRepository = organisationUnitRepository;
    }

    @Override
    public String entityName() {
        return ORGANISATION_UNIT;
    }

    public Optional<OrganisationUnit> getByName(String name) {
        return organisationUnitRepository.findByName(name);
    }

    public Optional<OrganisationUnit> findByName(String name) {
        return organisationUnitRepository.findByName(name);
    }
}
