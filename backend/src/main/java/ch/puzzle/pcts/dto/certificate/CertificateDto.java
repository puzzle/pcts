package ch.puzzle.pcts.dto.certificate;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

public record CertificateDto(
        @Schema(description = "The unique identifier of certificate.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The name of the certificate.", example = "Certified GitOps Associate", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,
        @Schema(description = "The amount of points the certificate provides.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) BigDecimal points,
        @Schema(description = "An optional comment for the certificate.", example = "This is an awesome certificate.", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment,
        @Schema(description = "Case insensitive tags that provide additional information about the certificate.", example = "[\"Important Tag\", \"GitLab\", \"Exam\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true, minLength = 1) List<String> tags) {
}
