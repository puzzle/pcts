package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceValidationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExperienceBusinessService {
    private final ExperienceValidationService validationService;
    private final ExperiencePersistenceService persistenceService;

    public ExperienceBusinessService(ExperienceValidationService validationService,
                                     ExperiencePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public List<Experience> getAll() {
        return persistenceService.getAll();
    }

    public Experience getById(Long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Experience with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    public Experience create(Experience experience) {
        validationService.validateOnCreate(experience);
        return persistenceService.save(experience);
    }

    public Experience update(Long id, Experience experience) {
        validationService.validateOnUpdate(id, experience);
        experience.setId(id);
        return persistenceService.save(experience);
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }
}
