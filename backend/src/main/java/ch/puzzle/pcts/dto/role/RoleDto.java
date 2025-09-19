package ch.puzzle.pcts.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoleDto(
        @Schema(description = "The unique identifier of the role.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, accessMode = Schema.AccessMode.READ_ONLY) Long id,

        @Schema(description = "The name of the role.", example = "Management", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,

        @Schema(description = "Indicates if the role has management privileges.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) boolean isManagement) {
}
