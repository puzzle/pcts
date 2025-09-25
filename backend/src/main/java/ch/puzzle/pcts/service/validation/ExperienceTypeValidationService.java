package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experience_type.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ExperienceTypeValidationService {
    private final ExperienceTypePersistenceService persistenceService;

    @Autowired
    public ExperienceTypeValidationService(ExperienceTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnGetById(Long id) {
        validateIfExists(id);
    }

    public void validateOnCreate(ExperienceType experienceType) {
        validateIfIdIsNull(experienceType.getId());
        validateName(experienceType.getName());
        validateExperienceTypePoints(experienceType);
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, ExperienceType experienceType) {
        validateIfExists(id);
        validateOnCreate(experienceType);
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
                                    ErrorKey.EXPERIENCE_TYPE_NAME_IS_NULL);
        }

        if (name.isBlank()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Name must not be empty",
                                    ErrorKey.EXPERIENCE_TYPE_NAME_IS_EMPTY);
        }
    }

    private void validateIfExists(long id) {
        persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "ExperienceType with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    private void validateExperienceTypePoints(ExperienceType experienceType) {
        if (experienceType.getHighlyRelevantPoints() == null || experienceType.getLimitedRelevantPoints() == null
            || experienceType.getLittleRelevantPoints() == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "ExperienceType has points with null as value",
                                    ErrorKey.EXPERIENCE_TYPE_POINTS_ARE_NULL);
        }
        if (experienceType.getHighlyRelevantPoints().signum() < 0
            || experienceType.getLimitedRelevantPoints().signum() < 0
            || experienceType.getLittleRelevantPoints().signum() < 0) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "ExperienceType has negative points",
                                    ErrorKey.EXPERIENCE_TYPE_POINTS_ARE_NEGATIVE);
        }
    }
}
