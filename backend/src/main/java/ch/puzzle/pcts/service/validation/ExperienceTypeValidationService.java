package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experienceType.ExperienceType;
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
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, ExperienceType experienceType) {
        validateIfExists(id);
        validateIfIdIsNull(experienceType.getId());
        validateName(experienceType.getName());
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

    // private void validateIfPointsArePositive(BigDecimal highlyRelevantPoints,
    // BigDecimal limitedRelevantPoints,
    // BigDecimal littleRelevantPoints) {
    // if (highlyRelevantPoints.signum() < 0|| limitedRelevantPoints.signum() < 0||
    // littleRelevantPoints.signum() < 0){
    //
    //
    // }
    //
    // }
}
