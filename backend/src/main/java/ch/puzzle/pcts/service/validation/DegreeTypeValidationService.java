package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
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
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    @Override
    public void validateOnUpdate(Long id, DegreeType degreeType) {
        super.validateOnUpdate(id, degreeType);
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, degreeType.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }
}
