package ch.puzzle.pcts.dto.calculation.degreecalculation;

import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Map;

public record DegreeCalculationDto(

        @Schema(description = "Unique identifier of the degree calculation", example = "42", requiredMode = Schema.RequiredMode.REQUIRED) Long id,

        @Schema(description = "Degree that is evaluated in the calculation", requiredMode = Schema.RequiredMode.REQUIRED) DegreeDto degree,

        @Schema(description = "Weight of each relevancy", requiredMode = Schema.RequiredMode.REQUIRED) Map<Relevancy, BigDecimal> relevancies,

        @Schema(description = "Optional comment explaining the relevancy", example = "Degree matches the job requirements exactly.") String comment) {
}
