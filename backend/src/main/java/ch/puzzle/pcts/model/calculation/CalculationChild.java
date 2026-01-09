package ch.puzzle.pcts.model.calculation;

import ch.puzzle.pcts.model.Model;

public interface CalculationChild {
    Calculation getCalculation();
    void setCalculation(Calculation calculation);
}
