package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreeType.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class DegreeTypeValidationService {
    private final DegreeTypePersistenceService persistenceService;

    @Autowired
    public DegreeTypeValidationService(DegreeTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnCreate(DegreeType degreeType) {
        validateIfIdIsNull(degreeType.getDegreeTypeId());
        validateName(degreeType.getName());
        validateRelevantPoints(degreeType.getHighlyRelevantPoints(),
                               degreeType.getLimitedRelevantPoints(),
                               degreeType.getLittleRelevantPoints());
    }

    public void validateOnGetById(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, DegreeType degreeType) {
        validateIfExists(id);
        validateIfIdIsNull(degreeType.getDegreeTypeId());
        validateName(degreeType.getName());
        validateRelevantPoints(degreeType.getHighlyRelevantPoints(),
                               degreeType.getLimitedRelevantPoints(),
                               degreeType.getLittleRelevantPoints());
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    private void validateIfExists(long id) {
        persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Degree type with degreeTypeId: " + id + " does not exist.",
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

    private void validateRelevantPoints(BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints,
                                        BigDecimal littleRelevantPoints) {
        validateHighlyRelevantPoints(highlyRelevantPoints);
        validateLimitedRelevantPointsPoints(limitedRelevantPoints);
        validateLittleRelevantPointsPoints(littleRelevantPoints);
    }

    private void validateHighlyRelevantPoints(BigDecimal highlyRelevantPoints) {
        if (highlyRelevantPoints == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "highlyRelevantPoints must not be null",
                                    ErrorKey.DEGREE_TYPE_HIGHLY_RELEVANT_POINTS_ARE_NULL);
        }

        if (highlyRelevantPoints.signum() == -1) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "highlyRelevantPoints must not be positive",
                                    ErrorKey.DEGREE_TYPE_HIGHLY_RELEVANT_POINTS_ARE_NEGATIVE);
        }

        if (highlyRelevantPoints.scale() > 2) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "highlyRelevantPoints must not have more than 2 decimal places",
                                    ErrorKey.DEGREE_TYPE_HIGHLY_RELEVANT_POINTS_HAVE_TOO_MANY_DECIMAL_PLACES);
        }
    }

    private void validateLimitedRelevantPointsPoints(BigDecimal limitedRelevantPoints) {
        if (limitedRelevantPoints == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "limitedRelevantPoints must not be null",
                                    ErrorKey.DEGREE_TYPE_LIMITED_RELEVANT_POINTS_ARE_NULL);
        }

        if (limitedRelevantPoints.signum() == -1) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "limitedRelevantPoints must not be positive",
                                    ErrorKey.DEGREE_TYPE_LIMITED_RELEVANT_POINTS_ARE_NEGATIVE);
        }

        if (limitedRelevantPoints.scale() > 2) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "limitedRelevantPoints must not have more than 2 decimal places",
                                    ErrorKey.DEGREE_TYPE_LIMITED_RELEVANT_POINTS_HAVE_TOO_MANY_DECIMAL_PLACES);
        }
    }

    private void validateLittleRelevantPointsPoints(BigDecimal littleRelevantPoints) {
        if (littleRelevantPoints == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "littleRelevantPoints must not be null",
                                    ErrorKey.DEGREE_TYPE_LITTLE_RELEVANT_POINTS_ARE_NULL);
        }

        if (littleRelevantPoints.signum() == -1) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "littleRelevantPoints must not be positive",
                                    ErrorKey.DEGREE_TYPE_LITTLE_RELEVANT_POINTS_ARE_NEGATIVE);
        }

        if (littleRelevantPoints.scale() > 2) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "littleRelevantPoints must not have more than 2 decimal places",
                                    ErrorKey.DEGREE_TYPE_LITTLE_RELEVANT_POINTS_HAVE_TOO_MANY_DECIMAL_PLACES);
        }
    }
}
