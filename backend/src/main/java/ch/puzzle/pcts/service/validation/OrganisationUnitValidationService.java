package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class OrganisationUnitValidationService {
    private final OrganisationUnitPersistenceService persistenceService;

    @Autowired
    public OrganisationUnitValidationService(OrganisationUnitPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnGetById(Long id) {
        validateIfExists(id);
    }

    public void validateOnCreate(OrganisationUnit organisationUnit) {
        validateIfIdIsNull(organisationUnit.getId());
        validateName(organisationUnit.getName());
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, OrganisationUnit organisationUnit) {
        validateIfExists(id);
        validateIfIdIsNull(organisationUnit.getId());
        validateName(organisationUnit.getName());
    }

    private void validateIfIdIsNull(Long id) {
        if (id != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id needs to be undefined", ErrorKey.ID_IS_NOT_NULL);
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Name must not be null",
                                    ErrorKey.ORGANIZATION_UNIT_NAME_IS_NULL);
        }

        if (name.isBlank()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Name must not be empty",
                                    ErrorKey.ORGANIZATION_UNIT_NAME_IS_EMPTY);
        }

        persistenceService
                .getByName(name)
                .orElseThrow(() -> new PCTSException(HttpStatus.BAD_REQUEST,
                                                     "Name already exists",
                                                     ErrorKey.ORGANIZATION_UNIT_NAME_ALREADY_EXISTS));
    }

    private void validateIfExists(long id) {
        persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Organisation Unit with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }
}
