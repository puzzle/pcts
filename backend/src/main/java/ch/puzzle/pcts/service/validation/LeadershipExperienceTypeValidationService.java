package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import ch.puzzle.pcts.service.persistence.LeadershipExperienceTypePersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceTypeValidationService extends ValidationBase<LeadershipExperienceType> {

    private final LeadershipExperienceTypePersistenceService persistenceService;

    public LeadershipExperienceTypeValidationService(LeadershipExperienceTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(LeadershipExperienceType leadershipExperience) {
        super.validateOnCreate(leadershipExperience);
        if (UniqueNameValidationUtil.nameAlreadyUsed(leadershipExperience.getName(), persistenceService::getByName)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        LEADERSHIP_EXPERIENCE_TYPE,
                        FieldKey.FIELD,
                        "name",
                        FieldKey.IS,
                        leadershipExperience.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }

    @Override
    public void validateOnUpdate(Long id, LeadershipExperienceType leadershipExperience) {
        super.validateOnUpdate(id, leadershipExperience);
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, leadershipExperience.getName(), persistenceService::getByName)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        LEADERSHIP_EXPERIENCE_TYPE,
                        FieldKey.FIELD,
                        "name",
                        FieldKey.IS,
                        leadershipExperience.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
