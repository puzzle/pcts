package ch.puzzle.pcts.dto.calculation.calculationleadershipexperience;

import io.swagger.v3.oas.annotations.media.Schema;

public record LeadershipExperienceCalculationInputDto(
        @Schema(description = "Unique identifier of the certificate calculation", nullable = true, example = "null") Long id,

        @Schema(description = "Id of the leadershipExperience associated with the calculation", example = "6") Long leadershipExperienceId) {
}