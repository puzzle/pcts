package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import org.springframework.stereotype.Service;

@Service
public class ExperienceValidationService {
    private final ExperiencePersistenceService persistenceService;

    public ExperienceValidationService(ExperiencePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnGetById(Long id) {
    }

    public void validateOnCreate(Experience experience) {

    }

    public void validateOnDelete(Long id) {
    }

    public void validateOnUpdate(Long id, Experience experience) {

    }
}
