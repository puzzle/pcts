package ch.puzzle.pcts.dto.calculation.degreecalculation;

import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record DegreeCalculationDto(

        @Schema(description = "Unique identifier of the degree calculation", example = "42", requiredMode = Schema.RequiredMode.REQUIRED) Long id,

        @Schema(description = "Degree that is evaluated in the calculation", requiredMode = Schema.RequiredMode.REQUIRED) DegreeDto degree,

        @Schema(description = "Weight of the degree in percent (1–100)", example = "50", minimum = "1", maximum = "100", requiredMode = Schema.RequiredMode.REQUIRED) BigDecimal weight,

        @Schema(description = "Relevancy level of the degree", example = "HIGHLY", requiredMode = Schema.RequiredMode.REQUIRED) Relevancy relevancy,

        @Schema(description = "Optional comment explaining the relevancy", example = "Degree matches the job requirements exactly.") String comment) {
}
