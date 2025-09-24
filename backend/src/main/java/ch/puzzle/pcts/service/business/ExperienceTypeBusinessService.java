package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experienceType.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceTypeValidationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypeBusinessService {
    private final ExperienceTypeValidationService validationService;
    private final ExperienceTypePersistenceService persistenceService;

    public ExperienceTypeBusinessService(ExperienceTypeValidationService validationService,
                                         ExperienceTypePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public List<ExperienceType> getAll() {
        return persistenceService.getAll();
    }

    public ExperienceType getById(long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "ExperienceType with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    public ExperienceType create(ExperienceType experienceType) {
        validationService.validateOnCreate(experienceType);
        return persistenceService.create(experienceType);
    }

    public ExperienceType update(Long id, ExperienceType experienceType) {
        validationService.validateOnUpdate(id, experienceType);
        return persistenceService.update(id, experienceType);
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }
}
