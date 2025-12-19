package ch.puzzle.pcts.dto.calculation.calculationleadershipexperience;

import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record LeadershipExperienceCalculationDto(
        @Schema(description = "Unique identifier of the leadership experience calculation", example = "42") Long id,

        @Schema(description = "Leadership experience associated with the calculation") LeadershipExperienceDto experience) {
}