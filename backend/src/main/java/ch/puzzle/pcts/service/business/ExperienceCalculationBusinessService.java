package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceCalculationValidationService;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;

import static ch.puzzle.pcts.Constants.EXPERIENCE_CALCULATION;

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
        return experienceCalculations.stream().map(this::calculatePoints).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculatePoints(ExperienceCalculation calculation) {
        Experience experience = calculation.getExperience();
        ExperienceType type = experience.getType();
        Relevancy relevancy = calculation.getRelevancy();

        BigDecimal basePoints = type.getPoints(relevancy);

        BigDecimal percentFactor = BigDecimal
                .valueOf(experience.getPercent())
                .divide(BigDecimal.valueOf(100), MathContext.UNLIMITED);

        long days = experience.getStartDate().until(experience.getEndDate(), ChronoUnit.DAYS);

        /*
         This is rounded because we have to round at some point otherwise this can cause a exception
         */
        BigDecimal years = BigDecimal.valueOf(days).divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);

        return basePoints.multiply(percentFactor).multiply(years);
    }

    @Override
    protected String entityName() {
        return EXPERIENCE_CALCULATION;
    }
}
