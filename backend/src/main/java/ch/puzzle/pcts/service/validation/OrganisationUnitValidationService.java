package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrganisationUnitValidationService extends ValidationBase<OrganisationUnit> {
    private final OrganisationUnitPersistenceService persistenceService;

    public OrganisationUnitValidationService(OrganisationUnitPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(OrganisationUnit organisationUnit) {
        super.validateOnCreate(organisationUnit);
        if (UniqueNameValidationUtil.nameAlreadyUsed(organisationUnit.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    @Override
    public void validateOnUpdate(Long id, OrganisationUnit organisationUnit) {
        super.validateOnUpdate(id, organisationUnit);
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, organisationUnit.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }
}
