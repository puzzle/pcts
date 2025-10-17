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
public class OrganisationUnitValidationService extends ValidationBase<OrganisationUnit> {
    private final OrganisationUnitPersistenceService persistenceService;

    @Autowired
    public OrganisationUnitValidationService(OrganisationUnitPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(OrganisationUnit organisationUnit) {
        super.validateOnCreate(organisationUnit);
        validateNameUniqueness(organisationUnit.getName());
    }

    @Override
    public void validateOnUpdate(Long id, OrganisationUnit organisationUnit) {
        super.validateOnUpdate(id, organisationUnit);
        validateNameUniqueExcludingSelf(id, organisationUnit.getName());
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
}
