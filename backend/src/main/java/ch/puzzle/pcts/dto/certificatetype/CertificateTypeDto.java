package ch.puzzle.pcts.dto.certificatetype;

import ch.puzzle.pcts.model.certificatetype.ExamType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CertificateTypeDto(
        @Schema(description = "The unique identifier of certificate-type.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = true) Long id,
        @Schema(description = "The name of the certificate-type.", example = "Certified GitOps Associate", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,
        @Schema(description = "The amount of points the certificate-type provides.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) BigDecimal points,
        @Schema(description = "An optional comment for the certificate-type.", example = "This is an awesome certificate-type.", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment,
        @Schema(description = "Case insensitive tags that provide additional information about the certificate-type.", example = "[\"Important Tag\", \"GitLab\", \"Exam\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true, minLength = 1) List<String> tags,
        @Schema(description = "The effort of the duration and the learning time combined in days", example = "5", requiredMode = Schema.RequiredMode.REQUIRED) Double effort,
        @Schema(description = "The duration of the exam in minutes", example = "120", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) Integer examDuration,
        @Schema(description = "An optional URL containing more information", example = "example.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String link,
        @Schema(description = "A predefined list of exam types", example = "NONE", requiredMode = Schema.RequiredMode.REQUIRED) ExamType examType,
        @Schema(description = "The name of the publisher", example = "Puzzle ITC", requiredMode = Schema.RequiredMode.REQUIRED) String publisher,
        @Schema(description = "Calculated status. False if link failed too often.", example = "true", accessMode = Schema.AccessMode.READ_ONLY) Boolean linkValid,
        @Schema(description = "Current number of failed retries.", example = "0", accessMode = Schema.AccessMode.READ_ONLY) Integer linkErrorCount,
        @Schema(description = "Timestamp of the last check.", example = "2024-02-14T10:15:30", accessMode = Schema.AccessMode.READ_ONLY) LocalDateTime linkLastCheckedAt) {
}
