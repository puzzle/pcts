package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.OrganisationUnitValidationService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OrganisationUnitBusinessService extends BusinessBase<OrganisationUnit> {
    private final OrganisationUnitPersistenceService persistenceService;

    public OrganisationUnitBusinessService(OrganisationUnitValidationService validationService,
                                           OrganisationUnitPersistenceService persistenceService) {
        super(validationService, persistenceService);
        this.persistenceService = persistenceService;
    }

    public List<OrganisationUnit> getAll() {
        return persistenceService.getAll();
    }

    public Optional<OrganisationUnit> findByName(String name) {
        return persistenceService.findByName(name);
    }
}
