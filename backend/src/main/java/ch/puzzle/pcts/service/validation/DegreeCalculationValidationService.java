package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.validation.util.CalculationChildValidationUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DegreeCalculationValidationService extends ValidationBase<DegreeCalculation> {
    @Override
    public void validate(DegreeCalculation model) {
        super.validate(model);
        validateMemberForCalculation(model);
        isValid(model);
    }

    public void validateDuplicateDegreeId(DegreeCalculation degreeCalculation,
                                          List<DegreeCalculation> degreeCalculationList) {
        if (CalculationChildValidationUtil
                .validateDuplicateCalculationChildId(degreeCalculation, degreeCalculationList)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CALCULATION,
                        FieldKey.FIELD,
                        "degree",
                        FieldKey.IS,
                        degreeCalculation.getDegree().getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.DUPLICATE_CALCULATION, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }

    public void validateMemberForCalculation(DegreeCalculation model) {
        Member degreeMember = model.getDegree().getMember();
        Member calculationMember = model.getCalculation().getMember();

        if (!degreeMember.equals(calculationMember)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "degree", FieldKey.CONDITION_FIELD, "member");

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_DOES_NOT_MATCH, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }

    public void isValid(DegreeCalculation model) {
        BigDecimal total = model.getRelevancies().values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!total.equals(BigDecimal.valueOf(100))) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "relevancies", FieldKey.IS, total.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTES_NOT_ADDING_UP_TO_100, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
