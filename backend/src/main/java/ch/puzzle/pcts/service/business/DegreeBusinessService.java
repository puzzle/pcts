package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DegreeBusinessService {

    private final DegreeValidationService validationService;
    private final DegreePersistenceService persistenceService;

    public DegreeBusinessService(DegreeValidationService validationService,
                                 DegreePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public Degree getById(Long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Member with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    public Degree create(Degree degree) {
        validationService.validateOnCreate(degree);
        return persistenceService.save(degree);
    }

    public Degree update(Long id, Degree degree) {
        validationService.validateOnUpdate(id, degree);
        degree.setId(id);
        return persistenceService.save(degree);
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }
}
