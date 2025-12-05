package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
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

    public List<ExperienceCalculation> getByCalculationId(Calculation calculation) {
        return this.experienceCalculationPersistenceService.getByCalculation(calculation);
    }

    @Override
    protected String entityName() {
        return CALCULATION;
    }
}
