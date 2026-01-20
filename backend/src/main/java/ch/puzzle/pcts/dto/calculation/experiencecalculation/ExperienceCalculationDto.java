package ch.puzzle.pcts.dto.calculation.experiencecalculation;

import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;

public record ExperienceCalculationDto(

        @Schema(description = "Unique identifier of the experience calculation", example = "42") Long id,

        @Schema(description = "Referenced experience") ExperienceDto experience,

        @Schema(description = "Relevancy of the experience for the calculation", example = "STRONGLY") Relevancy relevancy,

        @Schema(description = "Optional comment explaining the relevancy", example = "Same job role and technology stack", nullable = true) String comment) {
}
