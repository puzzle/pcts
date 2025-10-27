package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypeValidationService extends ValidationBase<ExperienceType> {
    private final ExperienceTypePersistenceService persistenceService;

    public ExperienceTypeValidationService(ExperienceTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(ExperienceType experienceType) {
        super.validateOnCreate(experienceType);
        if (UniqueNameValidationUtil.nameAlreadyUsed(experienceType.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    @Override
    public void validateOnUpdate(Long id, ExperienceType experienceType) {
        super.validateOnUpdate(id, experienceType);
        if (UniqueNameValidationUtil
                .nameExcludingSelfAlredyUsed(id, experienceType.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }
}
