package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.OrganisationUnitValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrganisationUnitBusinessService extends BusinessBase<OrganisationUnit> {

    public OrganisationUnitBusinessService(OrganisationUnitValidationService validationService,
                                           OrganisationUnitPersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    public List<OrganisationUnit> getAll() {
        return persistenceService.getAll();
    }
}
