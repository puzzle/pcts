package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DegreeCalculationValidationService extends CalculationChildValidationBase<DegreeCalculation> {
    @Override
    public void validateOnCreate(DegreeCalculation model) {
        super.validateOnCreate(model);
        validateMemberForCalculation(model);
        validateWeightsForCalculation(model);
    }

    @Override
    public void validateOnUpdate(Long id, DegreeCalculation model) {
        super.validateOnUpdate(id, model);
        validateMemberForCalculation(model);
        validateWeightsForCalculation(model);
    }

    public void validateDuplicateDegreeId(DegreeCalculation degreeCalculation,
                                          List<DegreeCalculation> degreeCalculationList) {
        validateNoDuplicate(degreeCalculation,
                            degreeCalculationList,
                            DEGREE,
                            calculationChild -> calculationChild.getDegree().getName());
    }

    public void validateMemberForCalculation(DegreeCalculation model) {
        validateMemberMatchesCalculation(model, DEGREE, calculationChild -> calculationChild.getDegree().getMember());
    }

    public void validateWeightsForCalculation(DegreeCalculation model) {
        BigDecimal total = model.getRelevancies().values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(BigDecimal.valueOf(100)) != 0) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "relevancies", FieldKey.IS, total.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTES_NOT_ADDING_UP_TO_100, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
