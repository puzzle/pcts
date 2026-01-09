package ch.puzzle.pcts.dto.calculation.experiencecalculation;

import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;

public record ExperienceCalculationInputDto(
        @Schema(description = "Unique identifier of the experience calculation", nullable = true, example = "null") Long id,

        @Schema(description = "ID of the experience to be linked", example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long experienceId,

        @Schema(description = "Relevancy of the experience", example = "HIGHLY", requiredMode = Schema.RequiredMode.REQUIRED) Relevancy relevancy,

        @Schema(description = "Optional justification for the selected relevancy", example = "It's highly relevant because it is the same job.", nullable = true) String comment) {
}
