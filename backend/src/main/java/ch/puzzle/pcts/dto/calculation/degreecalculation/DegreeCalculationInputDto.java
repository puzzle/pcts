package ch.puzzle.pcts.dto.calculation.degreecalculation;

import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Map;

public record DegreeCalculationInputDto(
        @Schema(description = "Unique identifier of the degree calculation", nullable = true, example = "null") Long id,

        @Schema(description = "ID of the degree being evaluated", example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long degreeId,

        @Schema(description = "Weight of each relevancy", requiredMode = Schema.RequiredMode.REQUIRED) Map<Relevancy, BigDecimal> relevancies,

        @Schema(description = "Optional comment explaining the relevancy and weight decision", example = "It's highly relevant because it matches the current position.") String comment) {
}
