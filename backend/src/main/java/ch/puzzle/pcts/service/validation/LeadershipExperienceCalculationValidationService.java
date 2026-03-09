package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.validation.util.CalculationChildValidationUtil;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceCalculationValidationService extends ValidationBase<LeadershipExperienceCalculation> {

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
        if (CalculationChildValidationUtil
                .validateDuplicateCalculationChildId(leadershipExperienceCalculation,
                                                     leadershipExperienceCalculationList)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CALCULATION,
                        FieldKey.FIELD,
                        "leadershipExperience",
                        FieldKey.IS,
                        leadershipExperienceCalculation
                                .getLeadershipExperience()
                                .getLeadershipExperienceType()
                                .getName());

            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List.of(new GenericErrorDto(ErrorKey.DUPLICATE_CALCULATION, attributes)));
        }
    }

    public void validateMemberForCalculation(LeadershipExperienceCalculation model) {
        Member leadershipExperienceMember = model.getLeadershipExperience().getMember();
        Member calculationMember = model.getCalculation().getMember();

        if (!leadershipExperienceMember.equals(calculationMember)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CALCULATION,
                        FieldKey.FIELD,
                        "leadershipExperience",
                        FieldKey.CONDITION_FIELD,
                        "member");

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_DOES_NOT_MATCH, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
