package ch.puzzle.pcts.dto.leadershipExperience;

import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record LeadershipExperienceDto(
        @Schema(description = "The unique identifier of the member certificate.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = false) Long id,

        @Schema(description = "The member associated with this certificate.", exampleClasses = MemberDto.class, requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) MemberDto member,

        @Schema(description = "The type of certificate awarded to the member.", exampleClasses = CertificateTypeDto.class, requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) CertificateTypeDto certificate,

        @Schema(description = "The date when the member completed the certificate.", example = "2025-09-24", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) LocalDate completedAt,

        @Schema(description = "The date until which the certificate is valid.", example = "2028-02-12", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) LocalDate validUntil,

        @Schema(description = "An optional comment for the member certificate.", example = "Completed via fast-track program", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment) {
}
