package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import ch.puzzle.pcts.service.validation.ExperienceCalculationValidationService;
import java.math.BigDecimal;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CalculationBusinessService extends BusinessBase<Calculation> {
    private final ExperienceCalculationBusinessService experienceCalculationBusinessService;
    private final ExperienceCalculationValidationService experienceCalculationValidationService;

    protected CalculationBusinessService(CalculationValidationService validationService,
                                         CalculationPersistenceService persistenceService,
                                         ExperienceCalculationBusinessService experienceCalculationBusinessService,
                                         ExperienceCalculationValidationService experienceCalculationValidationService) {
        super(validationService, persistenceService);
        this.experienceCalculationBusinessService = experienceCalculationBusinessService;
        this.experienceCalculationValidationService = experienceCalculationValidationService;
    }

    @Override
    public Calculation create(Calculation calculation) {
        calculation.getExperiences().forEach(experience -> {
            List<ExperienceCalculation> experienceCalculationList = this.experienceCalculationBusinessService
                    .getByExperience(experience.getExperience());
            experienceCalculationValidationService.validateDuplicateExperienceId(experience, experienceCalculationList);
        });

        validationService.validateOnCreate(calculation);
        calculation.getExperiences().forEach(experienceCalculationValidationService::validateOnCreate);

        Calculation createdCalculation = persistenceService.save(calculation);

        List<ExperienceCalculation> createdExperienceCalculations = new ArrayList<>();
        for (ExperienceCalculation experience : calculation.getExperiences()) {
            experience.setCalculation(createdCalculation);
            ExperienceCalculation created = experienceCalculationBusinessService.create(experience);

            createdExperienceCalculations.add(created);
        }

        createdCalculation.setExperiences(createdExperienceCalculations);
        return createdCalculation;
    }

    @Override
    public Calculation update(Long id, Calculation calculation) {
        if (persistenceService.getById(id).isEmpty()) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());
            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);
            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        }

        validationService.validateOnUpdate(id, calculation);

        calculation.setId(id);

        for (ExperienceCalculation exp : calculation.getExperiences()) {
            exp.setCalculation(calculation);
        }

        List<ExperienceCalculation> existingChildren = experienceCalculationBusinessService.getByCalculationId(id);

        for (ExperienceCalculation exp : calculation.getExperiences()) {

            Long expCalcId = experienceCalculationValidationService
                    .findIdByCalculationAndExperience(exp, existingChildren);

            experienceCalculationValidationService.validateOnUpdate(expCalcId, exp);
        }

        Calculation updatedCalculation = persistenceService.save(calculation);

        List<ExperienceCalculation> updatedChildren = new ArrayList<>();

        for (ExperienceCalculation exp : calculation.getExperiences()) {

            Long expCalcId = experienceCalculationValidationService
                    .findIdByCalculationAndExperience(exp, existingChildren);

            exp.setCalculation(updatedCalculation);

            ExperienceCalculation updated = experienceCalculationBusinessService.update(expCalcId, exp);

            updatedChildren.add(updated);
        }

        updatedCalculation.setExperiences(updatedChildren);
        return updatedCalculation;
    }

    @Override
    public Calculation getById(Long id) {

        BigDecimal totalRelevancyPoints = BigDecimal.ZERO;
        Optional<Calculation> calculation = persistenceService.getById(id);

        if (calculation.isEmpty()) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());
            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);
            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        }

        List<ExperienceCalculation> experienceCalculationList = experienceCalculationBusinessService
                .getByCalculationId(id);

        for (ExperienceCalculation experience : experienceCalculationList) {
            Relevancy relevancy = experience.getRelevancy();
            BigDecimal relevancyPoints;

            switch (relevancy) {
                case HIGHLY -> relevancyPoints = experience.getExperience().getType().getHighlyRelevantPoints();
                case LIMITED -> relevancyPoints = experience.getExperience().getType().getLimitedRelevantPoints();
                case LITTLE -> relevancyPoints = experience.getExperience().getType().getLittleRelevantPoints();
                default -> relevancyPoints = BigDecimal.ZERO;
            }

            relevancyPoints = relevancyPoints
                    .multiply(BigDecimal.valueOf(experience.getExperience().getPercent() / 100.0));
            int years = Period
                    .between(experience.getExperience().getStartDate(), experience.getExperience().getEndDate())
                    .getYears();
            relevancyPoints = relevancyPoints.multiply(BigDecimal.valueOf(years));

            totalRelevancyPoints = totalRelevancyPoints.add(relevancyPoints);
        }

        calculation.get().setPoints(totalRelevancyPoints);

        return calculation.get();
    }

    @Override
    protected String entityName() {
        return CALCULATION;
    }
}
