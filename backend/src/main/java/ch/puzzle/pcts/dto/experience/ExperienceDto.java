package ch.puzzle.pcts.dto.experience;

import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record ExperienceDto(
        @Schema(description = "The unique identifier of the experience.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The member associated with this experience.", exampleClasses = MemberDto.class, requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) MemberDto member,
        @Schema(description = "The name or title of the experience", example = "Software Engineer Intern", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,
        @Schema(description = "The employer or organization where the experience took place.", example = "TechCorp Inc.", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String employer,
        @Schema(description = "The percentage of workload or effort associated with this experience.", example = "100", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minimum = "0", maximum = "100") int percent,
        @Schema(description = "The type or category of the experience.", exampleClasses = ExperienceTypeDto.class, requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) ExperienceTypeDto type,
        @Schema(description = "Additional comments about the experience.", example = "Worked on backend API development using Spring Boot.", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment,
        @Schema(description = "The start date of the experience.", example = "2021-06-01", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) LocalDate startDate,
        @Schema(description = "The end date of the experience.", example = "2021-12-31", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) LocalDate endDate) {
}
