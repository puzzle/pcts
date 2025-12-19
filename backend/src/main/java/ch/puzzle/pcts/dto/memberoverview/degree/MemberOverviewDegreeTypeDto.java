package ch.puzzle.pcts.dto.memberoverview.degree;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberOverviewDegreeTypeDto(
        @Schema(description = "The name of the degree-type.", example = "Bachelor of Science", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name) {
}
