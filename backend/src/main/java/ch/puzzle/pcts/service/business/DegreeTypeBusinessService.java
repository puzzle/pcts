package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeTypeValidationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypeBusinessService {
    private final DegreeTypeValidationService validationService;
    private final DegreeTypePersistenceService persistenceService;

    public DegreeTypeBusinessService(DegreeTypeValidationService validationService,
                                     DegreeTypePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public List<DegreeType> getAll() {
        return persistenceService.getAll();
    }

    public DegreeType create(DegreeType degreeType) {
        validationService.validateOnCreate(degreeType);
        return persistenceService.save(degreeType);
    }

    public DegreeType getById(Long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Degree type with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    public DegreeType update(Long id, DegreeType degreeType) {
        validationService.validateOnUpdate(id, degreeType);
        return persistenceService.save(degreeType);
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }
}
