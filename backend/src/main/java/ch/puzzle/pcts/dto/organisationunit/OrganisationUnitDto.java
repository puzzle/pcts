package ch.puzzle.pcts.dto.organisationunit;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrganisationUnitDto(
        @Schema(description = "The unique identifier of the organisation unit.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The name of the organisation unit. Must be unique.", example = "/mobility", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name) {
}
