package ch.puzzle.pcts.dto.leadershipExperience;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

//todo add api docs
public record LeadershipExperienceInputDto(
        @Schema(description = "The member id that associated with this leadership experience.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long memberId,

        @Schema(description = "The type of leadership experience awarded to the member.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long leadershipExperienceTypeId,

        @Schema(description = "The date when the member completed the leadership experience.", example = "2025-09-24", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) LocalDate completedAt,

        @Schema(description = "The date until which the leadership experience is valid.", example = "2028-02-12", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) LocalDate validUntil,

        @Schema(description = "An optional comment for the member leadership experience.", example = "Completed via fast-track program", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment) {
}
