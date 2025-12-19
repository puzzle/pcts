package ch.puzzle.pcts.dto.memberoverview.leadershipexperience;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberOverviewLeadershipExperienceDto(
        @Schema(description = "The unique identifier of the leadership experience.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = false) Long id,

        @Schema(description = "The type of leadership experience awarded to the member.", exampleClasses = MemberOverviewLeadershipExperienceTypeDto.class, requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) MemberOverviewLeadershipExperienceTypeDto experience,

        @Schema(description = "An optional comment for the leadership experience.", example = "Completed via fast-track program", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment) {
}
