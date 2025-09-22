package ch.puzzle.pcts.dto.experienceType;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ExperienceTypeDto(
        @Schema(description = "The unique identifier of the experience-type", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The name of the experience-type.", example = "Management", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,
        @Schema(description = "The amount of highly relevant points of the experience-type", example = "10", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minimum = "0") BigDecimal highlyRelevantPoints,
        @Schema(description = "The amount of limited relevant points of the experience-type", example = "3", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minimum = "0") BigDecimal limitedRelevantPoints,
        @Schema(description = "The amount of little relevant points of the experience-type", example = "8", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minimum = "0") BigDecimal littleRelevantPoints) {
}
