package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.member.Member;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExperienceCalculationValidationService extends ValidationBase<ExperienceCalculation> {

    @Override
    public void validateOnCreate(ExperienceCalculation model) {
        super.validateOnCreate(model);
        this.validateMemberForCalculation(model);
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

    public void validateMemberForCalculation(ExperienceCalculation model) {
        Member experienceMember = model.getExperience().getMember();
        Member calculationMember = model.getCalculation().getMember();

        if (!experienceMember.equals(calculationMember)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "experience", FieldKey.CONDITION_FIELD, "member");

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_MATCHES, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
