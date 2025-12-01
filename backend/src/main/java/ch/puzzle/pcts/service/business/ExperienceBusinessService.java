package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.*;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExperienceBusinessService extends BusinessBase<Experience> {
    public ExperienceBusinessService(ExperienceValidationService validationService,
                                     ExperiencePersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    public List<Experience> getAll() {
        return persistenceService.getAll();
    }

    @Override
    protected String entityName() {
        return EXPERIENCE;
    }
}
