package ch.puzzle.pcts.dto.memberoverview.leadershipexperience;

import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberOverviewLeadershipExperienceTypeDto(
        @Schema(description = "The name of the leadership-experience-type.", example = "", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String name,
        @Schema(description = "The kind of leadership-experience-type which is either MILITARY_FUNCTION, YOUTH_AND_SPORT or LEADERSHIP_TRAINING", example = "LEADERSHIP_TRAINING", requiredMode = Schema.RequiredMode.REQUIRED) CertificateKind experienceKind) {
}
