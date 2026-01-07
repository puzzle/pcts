package ch.puzzle.pcts.dto.memberoverview.certificate;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record MemberOverviewCertificateDto(
        @Schema(description = "The unique identifier of the member certificate.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = false) Long id,
        @Schema(description = "The type of certificate awarded to the member.", exampleClasses = MemberOverviewCertificateTypeDto.class, requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) MemberOverviewCertificateTypeDto certificate,
        @Schema(description = "The date when the member completed the certificate.", example = "2025-09-24", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) LocalDate completedAt,
        @Schema(description = "An optional comment for the member certificate.", example = "Completed via fast-track program", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment) {
}
