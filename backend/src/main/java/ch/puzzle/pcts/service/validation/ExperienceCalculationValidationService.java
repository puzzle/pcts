package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.*;

import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExperienceCalculationValidationService extends CalculationChildValidationBase<ExperienceCalculation> {

    @Override
    public void validateOnCreate(ExperienceCalculation model) {
        super.validateOnCreate(model);
        validateMemberForCalculation(model);
    }

    @Override
    public void validateOnUpdate(Long id, ExperienceCalculation model) {
        super.validateOnUpdate(id, model);
        validateMemberForCalculation(model);
    }

    public void validateDuplicateExperienceId(ExperienceCalculation experienceCalculation,
                                              List<ExperienceCalculation> experienceCalculationList) {
        validateNoDuplicate(experienceCalculation,
                            experienceCalculationList,
                            EXPERIENCE,
                            calculationChild -> calculationChild.getExperience().getName());
    }

    public void validateMemberForCalculation(ExperienceCalculation model) {
        validateMemberMatchesCalculation(model,
                                         EXPERIENCE,
                                         calculationChild -> calculationChild.getExperience().getMember());
    }
}
