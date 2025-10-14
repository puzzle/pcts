package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.OrganisationUnitValidationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrganisationUnitBusinessService {
    private final OrganisationUnitValidationService validationService;
    private final OrganisationUnitPersistenceService persistenceService;

    public OrganisationUnitBusinessService(OrganisationUnitValidationService validationService,
                                           OrganisationUnitPersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public List<OrganisationUnit> getAll() {
        return persistenceService.getAll();
    }

    public OrganisationUnit getById(long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Organisation unit with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    public OrganisationUnit create(OrganisationUnit organisationUnit) {
        validationService.validateOnCreate(organisationUnit);
        return persistenceService.create(organisationUnit);
    }

    public OrganisationUnit update(Long id, OrganisationUnit organisationUnit) {
        validationService.validateOnUpdate(id, organisationUnit);
        return persistenceService.update(id, organisationUnit);
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }
}
