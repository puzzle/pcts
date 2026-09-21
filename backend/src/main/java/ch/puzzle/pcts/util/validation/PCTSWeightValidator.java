package ch.puzzle.pcts.util.validation;

import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PCTSWeightValidator implements ConstraintValidator<PCTSWeightValidation, Object> {
    private PCTSWeightValidation annotation;

    @Override
    public void initialize(PCTSWeightValidation constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        DegreeCalculation degreeCalculation = (DegreeCalculation) value;

        // return
        // degreeCalculation.getStrongWeight().add(degreeCalculation.getPartlyWeight()).add(degreeCalculation.getLessWeight())
        // == 100(degreeCalculation.);
        return false;
    }
}
