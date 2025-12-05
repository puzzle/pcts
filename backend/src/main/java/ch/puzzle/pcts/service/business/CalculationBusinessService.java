package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import ch.puzzle.pcts.service.validation.ExperienceCalculationValidationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        validationService.validateOnCreate(calculation);
        Calculation createdCalculation = persistenceService.save(calculation);

        List<ExperienceCalculation> createdExperienceCalculations = new ArrayList<>();

        for (ExperienceCalculation experience : createdCalculation.getExperiences()) {
            experience.setCalculation(createdCalculation);
            ExperienceCalculation created = this.experienceCalculationBusinessService.create(experience);
            createdExperienceCalculations.add(created);
        }

        createdCalculation.setExperiences(createdExperienceCalculations);
        return createdCalculation;
    }

    @Override
    public Calculation update(Long id, Calculation calculation) {
        if (persistenceService.getById(id).isPresent()) {
            validationService.validateOnUpdate(id, calculation);
            calculation.setId(id);
            Calculation updatedCalculation = persistenceService.save(calculation);

            List<ExperienceCalculation> updatedExperienceCalculations = new ArrayList<>();

            updatedCalculation.getExperiences().forEach(experience -> {
                experienceCalculationBusinessService.getByCalculationId(calculation);
                Long experienceCalculationId = this.experienceCalculationValidationService.matchIdPair(experience);
                experience.setCalculation(updatedCalculation);
                ExperienceCalculation updated = this.experienceCalculationBusinessService
                        .update(experienceCalculationId, experience);
                updatedExperienceCalculations.add(updated);
            });
            updatedCalculation.setExperiences(updatedExperienceCalculations);
            return updatedCalculation;

        } else {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));

        }
    }

    @Override
    protected String entityName() {
        return CALCULATION;
    }
}
