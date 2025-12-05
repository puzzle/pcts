package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.service.business.ExperienceCalculationBusinessService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExperienceCalculationValidationService extends ValidationBase<ExperienceCalculation> {
    private final ExperienceCalculationBusinessService businessService;

    ExperienceCalculationValidationService(ExperienceCalculationBusinessService businessService) {
        this.businessService = businessService;
    }

    public Long matchIdPair(ExperienceCalculation experienceCalculation) {
        List<ExperienceCalculation> experienceCalculationList = this.businessService
                .getByCalculationId(experienceCalculation.getCalculation());
        return experienceCalculationList
                .stream()
                .filter(ec -> ec.getCalculation().getId().equals(experienceCalculation.getCalculation().getId())
                              && ec.getExperience().getId().equals(experienceCalculation.getExperience().getId()))
                .map(ExperienceCalculation::getId)
                .findFirst()
                .orElse(null);
    }

    public void validateDuplicateExperienceId(ExperienceCalculation experienceCalculation) {
        // validate On create if experienceId is Duplicate to ensure no double counting
        // of member experience.
    }
}
