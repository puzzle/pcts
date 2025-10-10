package ch.puzzle.pcts.dto.leadership_experience;

import ch.puzzle.pcts.model.certificate.CertificateType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record LeadershipExperienceDto(
        @Schema(description = "The unique identifier of leadership-experience.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = true) Long id,
        @Schema(description = "The name of the leadership-experience.", example = "", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String name,
        @Schema(description = "The amount of points the leadership-experience grants.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) BigDecimal points,
        @Schema(description = "A optional comment for the leadership-experience.", example = "This is an awesome leadership-experience!", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment,
        @Schema(description = "The type of the Leadership either MILITARY_FUNCTION, YOUTH_AND_SPORT or LEADERSHIP_TRAINING", example = "LEADERSHIP_TRAINING", requiredMode = Schema.RequiredMode.REQUIRED) CertificateType certificateType) {
}
