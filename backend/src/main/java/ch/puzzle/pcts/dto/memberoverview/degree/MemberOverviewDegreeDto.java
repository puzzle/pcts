package ch.puzzle.pcts.dto.memberoverview.degree;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record MemberOverviewDegreeDto(
        @Schema(description = "The unique identifier of the degree.", example = "1", accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The name of the degree.", example = "Master of Computer Science", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String name,
        @Schema(description = "The name of the degree-type.", example = "Bachelor of Science", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String degreeTypeName,
        @Schema(description = "The start date of the degree program.", example = "2018-09-01", requiredMode = Schema.RequiredMode.REQUIRED) LocalDate startDate,
        @Schema(description = "The end date of the degree program.", example = "2022-06-30") LocalDate endDate) {
}
