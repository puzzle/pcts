package ch.puzzle.pcts.dto.leadershipexperience;

import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record LeadershipExperienceDto(
        @Schema(description = "The unique identifier of the leadership.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = false) Long id,

        @Schema(description = "The type of leadership.", exampleClasses = CertificateTypeDto.class, requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) LeadershipExperienceTypeDto experience,

        @Schema(description = "An optional comment for the leadership", example = "Completed via fast-track program", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment) {
}
