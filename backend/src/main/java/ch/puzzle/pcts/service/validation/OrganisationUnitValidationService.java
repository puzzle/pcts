package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import java.util.Optional;
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
        validateNameConstraints(organisationUnit.getName());
        validateNameUniqueness(organisationUnit.getName());
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, OrganisationUnit organisationUnit) {
        validateIfExists(id);
        validateIfIdIsNull(organisationUnit.getId());
        validateNameConstraints(organisationUnit.getName());
        validateNameUniqueExcludingSelf(id, organisationUnit.getName());
    }

    private void validateIfIdIsNull(Long id) {
        if (id != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id needs to be undefined", ErrorKey.ID_IS_NOT_NULL);
        }
    }

    private void validateNameConstraints(String name) {
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

        if (persistenceService.getByName(name) != null) {
            throw new PCTSException(HttpStatus.CONFLICT,
                                    "Name already exists",
                                    ErrorKey.ORGANIZATION_UNIT_NAME_ALREADY_EXISTS);
        }
    }

    private void validateNameUniqueness(String name) {
        persistenceService.getByName(name).ifPresent(organisationUnit -> {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Name already exists",
                                    ErrorKey.ORGANIZATION_UNIT_NAME_ALREADY_EXISTS);
        });
    }

    private void validateNameUniqueExcludingSelf(Long id, String name) {
        Optional<OrganisationUnit> existingOrganisationUnit = persistenceService.getByName(name);
        existingOrganisationUnit.ifPresent(organisationUnit -> {
            if (!organisationUnit.getId().equals(id)) {
                throw new PCTSException(HttpStatus.BAD_REQUEST,
                                        "Name already exists",
                                        ErrorKey.ORGANIZATION_UNIT_NAME_ALREADY_EXISTS);
            }
        });
    }

    private void validateIfExists(long id) {
        persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Organisation Unit with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }
}
