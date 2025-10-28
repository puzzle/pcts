package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreePersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DegreeValidationService {

    DegreePersistenceService persistenceService;

    DegreeValidationService(DegreePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnCreate(Degree degree) {

    }

    public void validateOnGetById(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, Degree degree) {
        validateIfExists(id);
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    private void validateIfExists(Long id) {
        persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Degree with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }
}
