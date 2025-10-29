package ch.puzzle.pcts.dto.experience;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record ExperienceInputDto(
        @Schema(description = "The id of the member associated with this experience.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long memberId,
        @Schema(description = "The name or title of the experience", example = "Software Engineer Intern", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,
        @Schema(description = "The employer or organization where the experience took place.", example = "TechCorp Inc.", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String employer,
        @Schema(description = "The percentage of workload or effort associated with this experience.", example = "100", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minimum = "0", maximum = "100") int percent,
        @Schema(description = "The id of the type of the experience.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long experienceTypeId,
        @Schema(description = "Additional comments about the experience.", example = "Worked on backend API development using Spring Boot.", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment,
        @Schema(description = "The start date of the experience.", example = "2021-06-01", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) LocalDate startDate,
        @Schema(description = "The end date of the experience.", example = "2021-12-31", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) LocalDate endDate) {
}
