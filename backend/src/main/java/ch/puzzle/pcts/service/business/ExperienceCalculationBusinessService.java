package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceCalculationValidationService;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
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

    public BigDecimal getExperiencePoints(Long id) {
        List<ExperienceCalculation> experienceCalculations = this.getByCalculationId(id);
        return experienceCalculations.stream().map(this::calculatePoints).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public ExperienceCalculation update(Long id, ExperienceCalculation experienceCalculation) {
        experienceCalculationValidationService.validateOnUpdate(id, experienceCalculation);
        List<ExperienceCalculation> existing = this.getByExperienceId(experienceCalculation.getExperience().getId());
        experienceCalculationValidationService.validateDuplicateExperienceId(experienceCalculation, existing);
        return experienceCalculationPersistenceService.save(experienceCalculation);
    }

    @Override
    public ExperienceCalculation create(ExperienceCalculation experienceCalculation) {
        experienceCalculationValidationService.validateOnCreate(experienceCalculation);
        List<ExperienceCalculation> existing = this.getByExperienceId(experienceCalculation.getExperience().getId());
        experienceCalculationValidationService.validateDuplicateExperienceId(experienceCalculation, existing);
        return experienceCalculationPersistenceService.save(experienceCalculation);
    }

    public List<ExperienceCalculation> createExperienceCalculations(Calculation calculation) {
        return calculation.getExperienceCalculations().stream().map(exp -> {
            exp.setCalculation(calculation);
            return this.create(exp);
        }).toList();
    }

    public List<ExperienceCalculation> updateExperienceCalculations(Calculation calculation) {
        List<ExperienceCalculation> existing = this.getByCalculationId(calculation.getId());

        List<ExperienceCalculation> experienceCalculations = calculation
                .getExperienceCalculations()
                .stream()
                .map(exp -> {
                    exp.setCalculation(calculation);
                    return exp.getId() == null ? this.create(exp) : this.update(exp.getId(), exp);
                })
                .toList();

        /*
         * Removing all created or updated experience calculations to later delete the
         * unused experience calculations
         */
        existing.removeAll(experienceCalculations);
        this.experienceCalculationPersistenceService
                .deleteAllByIdInBatch(existing.stream().map(ExperienceCalculation::getId).toList());

        return experienceCalculations;
    }

    /*
     * The divisions are rounded to a DECIMAL128 digit because numbers with a
     * infinite amount of digits could cause a ArithmeticException
     */
    private BigDecimal calculatePoints(ExperienceCalculation calculation) {
        Experience experience = calculation.getExperience();
        ExperienceType type = experience.getType();
        Relevancy relevancy = calculation.getRelevancy();

        BigDecimal basePoints = type.getPointsByRelevancy(relevancy);

        BigDecimal percentFactor = BigDecimal
                .valueOf(experience.getPercent())
                .divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);

        long days = experience
                .getStartDate()
                .until(getDateOrTodayIfDateIsNull(experience.getEndDate()), ChronoUnit.DAYS);

        BigDecimal years = BigDecimal.valueOf(days).divide(BigDecimal.valueOf(365), MathContext.DECIMAL128);

        return basePoints.multiply(percentFactor).multiply(years);
    }

    private LocalDate getDateOrTodayIfDateIsNull(LocalDate date) {
        if (date == null) {
            return LocalDate.now();
        }
        return date;
    }
}
