package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.EXPERIENCE_TYPE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import java.util.List;
import java.util.Map;
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
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        EXPERIENCE_TYPE,
                        FieldKey.FIELD,
                        "name",
                        FieldKey.IS,
                        experienceType.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }

    @Override
    public void validateOnUpdate(Long id, ExperienceType experienceType) {
        super.validateOnUpdate(id, experienceType);
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, experienceType.getName(), persistenceService::getByName)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        EXPERIENCE_TYPE,
                        FieldKey.FIELD,
                        "name",
                        FieldKey.IS,
                        experienceType.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));

        }
    }
}
