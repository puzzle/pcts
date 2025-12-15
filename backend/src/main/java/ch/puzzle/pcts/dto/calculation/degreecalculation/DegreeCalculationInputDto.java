package ch.puzzle.pcts.dto.calculation.degreecalculation;

import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record DegreeCalculationInputDto(@Schema(example = "1") Long degreeId, @Schema(example = "1") BigDecimal weight,
        @Schema(example = "HIGHLY") Relevancy relevancy,
        @Schema(example = "It's highly because it is the same job.") String comment) {
}
