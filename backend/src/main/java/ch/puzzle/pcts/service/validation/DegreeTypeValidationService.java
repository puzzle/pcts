package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreeType.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DegreeTypeValidationService {
    private final DegreeTypePersistenceService persistenceService;

    @Autowired
    public DegreeTypeValidationService(DegreeTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnCreate(DegreeType degreeType) {
        validateIfIdIsNull(degreeType.getId());
        validateName(degreeType.getName());
    }

    public void validateOnGetById(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, DegreeType degreeType) {
        validateIfExists(id);
        validateIfIdIsNull(degreeType.getId());
        validateName(degreeType.getName());
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    private void validateIfExists(long id) {
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
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be empty", ErrorKey.DEGREE_TYPE_NAME_IS_EMPTY);
        }
    }

    private void validateRelevantPoints(BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints, BigDecimal littleRelevantPoints) {
        if ()
    }
}
