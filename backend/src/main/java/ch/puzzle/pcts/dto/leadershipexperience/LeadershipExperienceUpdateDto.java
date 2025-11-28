package ch.puzzle.pcts.dto.leadershipexperience;

import io.swagger.v3.oas.annotations.media.Schema;

public record LeadershipExperienceUpdateDto(
        @Schema(description = "The type of leadership experience awarded to the member.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long leadershipExperienceTypeId,

        @Schema(description = "An optional comment for the member leadership experience.", example = "Completed via fast-track program", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment) {
}
