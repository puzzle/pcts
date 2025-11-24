package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.DEGREE_TYPE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypeValidationService extends ValidationBase<DegreeType> {
    private final DegreeTypePersistenceService persistenceService;

    public DegreeTypeValidationService(DegreeTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(DegreeType degreeType) {
        super.validateOnCreate(degreeType);
        if (UniqueNameValidationUtil.nameAlreadyUsed(degreeType.getName(), persistenceService::getByName)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, DEGREE_TYPE, FieldKey.FIELD, "name", FieldKey.IS, degreeType.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));

        }
    }

    @Override
    public void validateOnUpdate(Long id, DegreeType degreeType) {
        super.validateOnUpdate(id, degreeType);
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, degreeType.getName(), persistenceService::getByName)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, DEGREE_TYPE, FieldKey.FIELD, "name", FieldKey.IS, degreeType.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));

        }
    }
}
