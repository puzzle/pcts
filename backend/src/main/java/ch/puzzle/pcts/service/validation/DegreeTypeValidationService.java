package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree_type.DegreeType;
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

    private void validateIfExists(Long id) {
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
        if (highlyRelevantPoints == null || limitedRelevantPoints == null || littleRelevantPoints == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "relevant points must not be null",
                                    ErrorKey.DEGREE_TYPE_RELEVANT_POINTS_ARE_NULL);
        }

        if (highlyRelevantPoints.signum() < 0 || limitedRelevantPoints.signum() < 0
            || littleRelevantPoints.signum() < 0) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "relevant points must not be negative",
                                    ErrorKey.DEGREE_TYPE_RELEVANT_POINTS_ARE_NEGATIVE);
        }

        if (highlyRelevantPoints.scale() > 2 || limitedRelevantPoints.scale() > 2 || littleRelevantPoints.scale() > 2) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "relevant points must not have more than 2 decimal places",
                                    ErrorKey.DEGREE_TYPE_RELEVANT_POINTS_HAVE_TOO_MANY_DECIMAL_PLACES);
        }
    }
}
