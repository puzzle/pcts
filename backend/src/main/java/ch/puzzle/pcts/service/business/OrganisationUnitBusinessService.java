package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.ORGANISATION_UNIT;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.OrganisationUnitRepository;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.OrganisationUnitValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrganisationUnitBusinessService
        extends
            BusinessBase<OrganisationUnit, OrganisationUnitRepository, OrganisationUnitValidationService, OrganisationUnitPersistenceService> {

    public OrganisationUnitBusinessService(OrganisationUnitValidationService validationService,
                                           OrganisationUnitPersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    public List<OrganisationUnit> getAll() {
        return persistenceService.getAll();
    }

    @Override
    protected String entityName() {
        return ORGANISATION_UNIT;
    }
}
