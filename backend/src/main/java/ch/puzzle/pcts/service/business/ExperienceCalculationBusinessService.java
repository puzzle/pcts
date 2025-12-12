package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceCalculationValidationService;
import java.math.BigDecimal;
import java.time.Period;
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

    public List<ExperienceCalculation> getByExperienceId(Long experienceId) {
        return this.experienceCalculationPersistenceService.getByExperienceId(experienceId);
    }

    public BigDecimal getExperiencePoints(List<ExperienceCalculation> experienceCalculations) {
        BigDecimal totalExperienceRelevancyPoints = BigDecimal.ZERO;

        for (ExperienceCalculation calculation : experienceCalculations) {
            Experience experience = calculation.getExperience();
            ExperienceType type = experience.getType();
            Relevancy relevancy = calculation.getRelevancy();

            BigDecimal basePoints = switch (relevancy) {
                case HIGHLY -> type.getHighlyRelevantPoints();
                case LIMITED -> type.getLimitedRelevantPoints();
                case LITTLE -> type.getLittleRelevantPoints();
                default -> BigDecimal.ZERO;
            };

            BigDecimal percentFactor = BigDecimal.valueOf(experience.getPercent()).divide(BigDecimal.valueOf(100));

            int years = Period.between(experience.getStartDate(), experience.getEndDate()).getYears();
            BigDecimal result = basePoints.multiply(percentFactor).multiply(BigDecimal.valueOf(years));

            totalExperienceRelevancyPoints = totalExperienceRelevancyPoints.add(result);
        }
        return totalExperienceRelevancyPoints;
    }

    @Override
    protected String entityName() {
        return CALCULATION;
    }
}
