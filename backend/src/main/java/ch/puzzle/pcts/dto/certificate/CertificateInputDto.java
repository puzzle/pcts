package ch.puzzle.pcts.dto.certificate;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record CertificateInputDto(
        @Schema(description = "The member id that associated with this certificate.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long memberId,

        @Schema(description = "The type of certificate awarded to the member.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long certificateTypeId,

        @Schema(description = "The date when the member completed the certificate.", example = "2025-09-24", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) LocalDate completedAt,

        @Schema(description = "The date until which the certificate is valid.", example = "2028-02-12", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) LocalDate validUntil,

        @Schema(description = "An optional comment for the member certificate.", example = "Completed via fast-track program", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment) {
}
