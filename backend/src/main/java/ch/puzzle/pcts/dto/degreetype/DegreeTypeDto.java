package ch.puzzle.pcts.dto.degreetype;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record DegreeTypeDto(
        @Schema(description = "The unique identifier of the degree-type.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The name of the degree-type.", example = "Bachelor of Science", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,
        @Schema(description = "The amount of points of the degree-type if the degree has high relevancy.", example = "10", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minimum = "0") BigDecimal highlyRelevantPoints,
        @Schema(description = "The amount of points of the degree-type if the degree has limited relevancy.", example = "7.67", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minimum = "0") BigDecimal limitedRelevantPoints,
        @Schema(description = "The amount of points of the degree-type if the degree has little relevancy.", example = "3.5", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minimum = "0") BigDecimal littleRelevantPoints) {
}