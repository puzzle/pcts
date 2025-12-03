package ch.puzzle.pcts.dto.leadershipexperiencetype;

import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record LeadershipExperienceTypeDto(
        @Schema(description = "The unique identifier of leadership-experience-type.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = true) Long id,
        @Schema(description = "The name of the leadership-experience-type.", example = "", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String name,
        @Schema(description = "The amount of points the leadership-experience-type grants.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) BigDecimal points,
        @Schema(description = "A optional comment for the leadership-experience-type.", example = "This is an awesome leadership-experience-type!", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment,
        @Schema(description = "The kind of leadership-experience-type which is either MILITARY_FUNCTION, YOUTH_AND_SPORT or LEADERSHIP_TRAINING", example = "LEADERSHIP_TRAINING", requiredMode = Schema.RequiredMode.REQUIRED) CertificateKind experienceKind) {
}
