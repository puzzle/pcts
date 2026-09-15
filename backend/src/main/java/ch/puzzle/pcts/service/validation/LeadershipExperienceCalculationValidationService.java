package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE;

import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceCalculationValidationService
        extends
            CalculationChildValidationBase<LeadershipExperienceCalculation> {

    @Override
    public void validateOnCreate(LeadershipExperienceCalculation model) {
        super.validateOnCreate(model);
        validateMemberForCalculation(model);
    }

    @Override
    public void validateOnUpdate(Long id, LeadershipExperienceCalculation model) {
        super.validateOnUpdate(id, model);
        validateMemberForCalculation(model);
    }

    public void validateDuplicateLeadershipExperienceId(LeadershipExperienceCalculation leadershipExperienceCalculation,
                                                        List<LeadershipExperienceCalculation> leadershipExperienceCalculationList) {
        validateNoDuplicate(leadershipExperienceCalculation,
                            leadershipExperienceCalculationList,
                            LEADERSHIP_EXPERIENCE,
                            calculationChild -> calculationChild
                                    .getLeadershipExperience()
                                    .getLeadershipExperienceType()
                                    .getName());
    }

    public void validateMemberForCalculation(LeadershipExperienceCalculation model) {
        validateMemberMatchesCalculation(model,
                                         LEADERSHIP_EXPERIENCE,
                                         calculationChild -> calculationChild.getLeadershipExperience().getMember());
    }
}
