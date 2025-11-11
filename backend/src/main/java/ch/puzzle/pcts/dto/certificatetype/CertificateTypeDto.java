package ch.puzzle.pcts.dto.certificatetype;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

public record CertificateTypeDto(
        @Schema(description = "The unique identifier of certificate-type.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = true) Long id,
        @Schema(description = "The name of the certificate-type.", example = "Certified GitOps Associate", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,
        @Schema(description = "The amount of points the certificate-type provides.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) BigDecimal points,
        @Schema(description = "An optional comment for the certificate-type.", example = "This is an awesome certificate-type.", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment,
        @Schema(description = "Case insensitive tags that provide additional information about the certificate-type.", example = "[\"Important Tag\", \"GitLab\", \"Exam\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true, minLength = 1) List<String> tags) {
}
