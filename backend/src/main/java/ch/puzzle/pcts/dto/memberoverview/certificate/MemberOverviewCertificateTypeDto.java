package ch.puzzle.pcts.dto.memberoverview.certificate;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberOverviewCertificateTypeDto(
        @Schema(description = "The name of the certificate-type.", example = "Certified GitOps Associate", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name) {
}
