package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.ORGANISATION_UNIT;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import java.util.List;
import java.util.Map;
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
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        ORGANISATION_UNIT,
                        FieldKey.FIELD,
                        "name",
                        FieldKey.IS,
                        organisationUnit.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));

        }
    }

    @Override
    public void validateOnUpdate(Long id, OrganisationUnit organisationUnit) {
        super.validateOnUpdate(id, organisationUnit);
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, organisationUnit.getName(), persistenceService::getByName)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        ORGANISATION_UNIT,
                        FieldKey.FIELD,
                        "name",
                        FieldKey.IS,
                        organisationUnit.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
