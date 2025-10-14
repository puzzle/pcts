package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypeValidationService {
    private final DegreeTypePersistenceService persistenceService;

    public DegreeTypeValidationService(DegreeTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnCreate(DegreeType degreeType) {
        validateIfIdIsNull(degreeType.getId());
        validateName(degreeType.getName());
        validatePoints(degreeType);
    }

    public void validateOnGetById(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, DegreeType degreeType) {
        validateIfExists(id);
        validateIfIdIsNull(degreeType.getId());
        validateName(degreeType.getName());
        validatePoints(degreeType);
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    private void validateIfExists(Long id) {
        persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Degree type with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    private void validateIfIdIsNull(Long id) {
        if (id != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id needs to be undefined", ErrorKey.ID_IS_NOT_NULL);
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be null", ErrorKey.DEGREE_TYPE_NAME_IS_NULL);
        }

        if (name.isBlank()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Name must not be empty",
                                    ErrorKey.DEGREE_TYPE_NAME_IS_EMPTY);
        }
    }

    private void validatePoints(DegreeType degreeType) {
        if (degreeType.getHighlyRelevantPoints() == null || degreeType.getLimitedRelevantPoints() == null
            || degreeType.getLittleRelevantPoints() == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "points must not be null",
                                    ErrorKey.DEGREE_TYPE_POINTS_ARE_NULL);
        }

        if (degreeType.getHighlyRelevantPoints().signum() < 0 || degreeType.getLimitedRelevantPoints().signum() < 0
            || degreeType.getLittleRelevantPoints().signum() < 0) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "points must not be negative",
                                    ErrorKey.DEGREE_TYPE_POINTS_ARE_NEGATIVE);
        }
    }
}
