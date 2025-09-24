package ch.puzzle.pcts.dto.degree_type;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record DegreeTypeDto(
        @Schema(description = "The unique identifier of degree-type.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The name of the degree-type.", example = "Bachelor's Degree", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,
        @Schema(description = "The amount of highly relevant points the degree-type provides", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) BigDecimal highlyRelevantPoints,
        @Schema(description = "The amount of limited relevant points the degree-type provides", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) BigDecimal limitedRelevantPoints,
        @Schema(description = "The amount of little relevant points the degree-type provides", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) BigDecimal littleRelevantPoints) {
}