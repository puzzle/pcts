package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.EXPERIENCE_CALCULATION;

import ch.puzzle.pcts.model.calculation.Calculation;
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

@Service
public class ExperienceCalculationBusinessService extends BusinessBase<ExperienceCalculation> {
    private final ExperienceCalculationPersistenceService experienceCalculationPersistenceService;
    private final ExperienceCalculationValidationService experienceCalculationValidationService;


    protected ExperienceCalculationBusinessService(ExperienceCalculationValidationService validationService,
                                                   ExperienceCalculationPersistenceService persistenceService) {
        super(validationService, persistenceService);
        this.experienceCalculationPersistenceService = persistenceService;
        this.experienceCalculationValidationService = validationService;
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

    @Override
    public ExperienceCalculation create(ExperienceCalculation experienceCalculation){
        List<ExperienceCalculation> existing = this
                .getByExperienceId(experienceCalculation.getExperience().getId());
        experienceCalculationValidationService.validateOnCreate(experienceCalculation);
        experienceCalculationValidationService.validateDuplicateExperienceId(experienceCalculation, existing);
        return experienceCalculationPersistenceService.save(experienceCalculation);
    }

    public List<ExperienceCalculation> createExperienceCalculations(Calculation calculation){
        return calculation.getExperiences().stream().map(exp -> {
            exp.setCalculation(calculation);
            return this.create(exp);
        }).toList();
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
         * This is rounded because we have to round at some point otherwise this can
         * cause a exception
         */
        BigDecimal years = BigDecimal.valueOf(days).divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);

        return basePoints.multiply(percentFactor).multiply(years);
    }

    @Override
    protected String entityName() {
        return EXPERIENCE_CALCULATION;
    }
}
