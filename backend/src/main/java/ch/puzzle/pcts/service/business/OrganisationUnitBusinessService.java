package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.OrganisationUnitValidationService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OrganisationUnitBusinessService extends BusinessBase<OrganisationUnit> {
    private final OrganisationUnitPersistenceService organisationUnitPersistenceService;

    public OrganisationUnitBusinessService(OrganisationUnitValidationService validationService,
                                           OrganisationUnitPersistenceService organisationUnitPersistenceService) {
        super(validationService, organisationUnitPersistenceService);
        this.organisationUnitPersistenceService = organisationUnitPersistenceService;
    }

    public List<OrganisationUnit> getAll() {
        return organisationUnitPersistenceService.getAll();
    }

    public Optional<OrganisationUnit> findByName(String name) {
        return organisationUnitPersistenceService.findByName(name);
    }
}
