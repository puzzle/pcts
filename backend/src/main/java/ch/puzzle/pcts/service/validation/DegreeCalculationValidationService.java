package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.DEGREE;

import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DegreeCalculationValidationService extends CalculationChildValidationBase<DegreeCalculation> {
    @Override
    public void validateOnCreate(DegreeCalculation model) {
        super.validateOnCreate(model);
        validateMemberForCalculation(model);
    }

    @Override
    public void validateOnUpdate(Long id, DegreeCalculation model) {
        super.validateOnUpdate(id, model);
        validateMemberForCalculation(model);
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
}
