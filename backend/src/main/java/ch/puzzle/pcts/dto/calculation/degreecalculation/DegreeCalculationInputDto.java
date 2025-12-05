package ch.puzzle.pcts.dto.calculation.degreecalculation;

import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;

public record DegreeCalculationInputDto(@Schema(example = "1") Long id, @Schema(example = "1") int weight,
        @Schema(example = "highly") Relevancy relevancy) {
}
