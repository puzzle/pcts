package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.service.persistence.ExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceCalculationValidationService;
import org.springframework.stereotype.Service;

@Service
public class ExperienceCalculationBusinessService extends BusinessBase<ExperienceCalculation> {

    protected ExperienceCalculationBusinessService(ExperienceCalculationValidationService validationService,
                                                   ExperienceCalculationPersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    @Override
    protected String entityName() {
        return CALCULATION;
    }
}
