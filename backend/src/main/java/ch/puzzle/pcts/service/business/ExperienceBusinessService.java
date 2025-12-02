package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.*;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceValidationService;
import org.springframework.stereotype.Service;

@Service
public class ExperienceBusinessService extends BusinessBase<Experience> {
    public ExperienceBusinessService(ExperienceValidationService validationService,
                                     ExperiencePersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    @Override
    protected String entityName() {
        return EXPERIENCE;
    }
}
