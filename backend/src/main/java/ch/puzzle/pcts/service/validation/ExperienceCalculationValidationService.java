package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExperienceCalculationValidationService extends ValidationBase<ExperienceCalculation> {

    ExperienceCalculationValidationService() {
    }

    public Long findIdByCalculationAndExperience(ExperienceCalculation experienceCalculation,
                                                 List<ExperienceCalculation> experienceCalculationList) {
        return experienceCalculationList
                .stream()
                .filter(ec -> ec.getCalculation().getId().equals(experienceCalculation.getCalculation().getId())
                              && ec.getExperience().getId().equals(experienceCalculation.getExperience().getId()))
                .map(ExperienceCalculation::getId)
                .findFirst()
                .orElse(null);
    }

    public void validateDuplicateExperienceId(ExperienceCalculation experienceCalculation,
                                              List<ExperienceCalculation> experienceCalculationList) {
        if (!experienceCalculationList.isEmpty()) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CALCULATION,
                        FieldKey.FIELD,
                        "experience",
                        FieldKey.IS,
                        experienceCalculation.getExperience().getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.DUPLICATE_CALCULATION, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
