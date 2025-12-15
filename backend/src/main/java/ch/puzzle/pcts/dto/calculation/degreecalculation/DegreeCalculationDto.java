package ch.puzzle.pcts.dto.calculation.degreecalculation;

import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.model.calculation.Relevancy;
import java.math.BigDecimal;

public record DegreeCalculationDto(Long id, DegreeDto degree, BigDecimal weight, Relevancy relevancy, String comment) {
}