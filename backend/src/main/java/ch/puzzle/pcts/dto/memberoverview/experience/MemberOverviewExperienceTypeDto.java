package ch.puzzle.pcts.dto.memberoverview.experience;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberOverviewExperienceTypeDto(
        @Schema(description = "The name of the experience-type.", example = "Management", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name) {
}
