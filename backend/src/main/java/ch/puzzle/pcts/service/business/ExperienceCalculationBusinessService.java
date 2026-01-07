package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.EXPERIENCE_CALCULATION;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
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
import java.util.Map;
import org.springframework.http.HttpStatus;
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
    public ExperienceCalculation create(ExperienceCalculation experienceCalculation) {
        List<ExperienceCalculation> existing = this.getByExperienceId(experienceCalculation.getExperience().getId());
        experienceCalculationValidationService.validateOnCreate(experienceCalculation);
        experienceCalculationValidationService.validateDuplicateExperienceId(experienceCalculation, existing);
        return experienceCalculationPersistenceService.save(experienceCalculation);
    }

    @Override
    public ExperienceCalculation update(Long id, ExperienceCalculation experienceCalculation) {
        if (persistenceService.getById(id).isEmpty()) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());
            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);
            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        }
        experienceCalculationValidationService.validateOnUpdate(id, experienceCalculation);
        experienceCalculationValidationService.validateMemberForCalculation(experienceCalculation);
        experienceCalculation.setId(id);
        return experienceCalculationPersistenceService.save(experienceCalculation);
    }

    public List<ExperienceCalculation> createExperienceCalculations(Calculation calculation) {
        return calculation.getExperiences().stream().map(exp -> {
            exp.setCalculation(calculation);
            return this.create(exp);
        }).toList();
    }

    public List<ExperienceCalculation> updateExperienceCalculations(Calculation calculation) {
        List<ExperienceCalculation> existing = this.getByCalculationId(calculation.getId());

        List<ExperienceCalculation> experienceCalculations = calculation.getExperiences().stream().map(exp -> {
            exp.setCalculation(calculation);
            return exp.getId() == null ? this.create(exp) : this.update(exp.getId(), exp);
        }).toList();

        /*
         * Removing all created or updated experience calculations to later delete the
         * unused experience calculations
         */
        existing.removeAll(experienceCalculations);
        existing.stream().map(ExperienceCalculation::getId).forEach(this::delete);

        return experienceCalculations;
    }

    // private Optional<Long> findIdByCalculationAndExperience(ExperienceCalculation
    // experienceCalculation,
    // List<ExperienceCalculation> experienceCalculationList) {
    // return experienceCalculationList
    // .stream()
    // .filter(ec ->
    // ec.getCalculation().getId().equals(experienceCalculation.getCalculation().getId())
    // &&
    // ec.getExperience().getId().equals(experienceCalculation.getExperience().getId()))
    // .map(ExperienceCalculation::getId)
    // .findFirst();
    // }

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

    @Override
    protected String entityName() {
        return EXPERIENCE_CALCULATION;
    }
}
