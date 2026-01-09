package ch.puzzle.pcts.dto.calculation.degreecalculation;

import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record DegreeCalculationInputDto(
        @Schema(description = "Unique identifier of the degree calculation", nullable = true, example = "null") Long id,

        @Schema(description = "ID of the degree being evaluated", example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long degreeId,

        @Schema(description = "Weight of the degree in percent (1–100)", example = "25", minimum = "1", maximum = "100", requiredMode = Schema.RequiredMode.REQUIRED) BigDecimal weight,

        @Schema(description = "Relevancy level of the degree", example = "HIGHLY", requiredMode = Schema.RequiredMode.REQUIRED) Relevancy relevancy,

        @Schema(description = "Optional comment explaining the relevancy decision", example = "It's highly relevant because it matches the current position.") String comment) {
}
