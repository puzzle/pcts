package ch.puzzle.pcts.dto.degree;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO representing a degree input.")
public record DegreeInputDto(
        @Schema(description = "The unique identifier of the member associated with the degree.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long memberId,

        @Schema(description = "The name of the degree.", example = "Mathematics Master", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String name,

        @Schema(description = "The institution granting the degree.", example = "Harvard University") String institution,

        @Schema(description = "Indicates whether the degree has been completed.", example = "true") Boolean completed,

        @Schema(description = "The unique identifier of the type of degree.", example = "3") Long typeId,

        @Schema(description = "The start date of the degree program.", example = "2019-09-01") Date startDate,

        @Schema(description = "The end date of the degree program.", example = "2021-06-30") Date endDate,

        @Schema(description = "Additional comments about the degree.", example = "Graduated with honors.") String comment

) {
}
