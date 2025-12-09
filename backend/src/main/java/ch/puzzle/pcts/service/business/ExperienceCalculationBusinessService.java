package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.persistence.ExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceCalculationValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExperienceCalculationBusinessService extends BusinessBase<ExperienceCalculation> {
    private final ExperienceCalculationPersistenceService experienceCalculationPersistenceService;

    protected ExperienceCalculationBusinessService(ExperienceCalculationValidationService validationService,
                                                   ExperienceCalculationPersistenceService persistenceService) {
        super(validationService, persistenceService);
        this.experienceCalculationPersistenceService = persistenceService;
    }

    public List<ExperienceCalculation> getByCalculationId(Long calculationId) {
        return this.experienceCalculationPersistenceService.getByCalculationId(calculationId);
    }

    public List<ExperienceCalculation> getByExperience(Experience experience) {
        return this.experienceCalculationPersistenceService.getByExperience(experience);
    }

    @Override
    protected String entityName() {
        return CALCULATION;
    }
}
