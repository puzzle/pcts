package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import org.springframework.stereotype.Service;

@Service
public class CalculationBusinessService extends BusinessBase<Calculation> {
    private final ExperienceCalculationBusinessService experienceCalculationBusinessService;

    protected CalculationBusinessService(CalculationValidationService validationService,
                                         CalculationPersistenceService persistenceService,
                                         ExperienceCalculationBusinessService experienceCalculationBusinessService) {
        super(validationService, persistenceService);
        this.experienceCalculationBusinessService = experienceCalculationBusinessService;
    }

    @Override
    public Calculation create(Calculation calculation) {
        validationService.validateOnCreate(calculation);
        Calculation createdCalculation = persistenceService.save(calculation);

        createdCalculation.getExperiences().forEach(experience -> {
            experience.setCalculation(createdCalculation);
            this.experienceCalculationBusinessService.create(experience);
        });

        return createdCalculation;
    }

    @Override
    protected String entityName() {
        return CALCULATION;
    }
}
