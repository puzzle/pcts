package ch.puzzle.pcts.dto.member;

import ch.puzzle.pcts.model.member.EmploymentState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

public record MemberInputDto(
        @Schema(description = "The unique identifier of the member.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The name of the name member.", example = "Susi", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String name,
        @Schema(description = "The last name of the member.", example = "Sommer", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) String lastName,
        @Schema(description = "The employment state of the member.", example = "APPLICANT", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) EmploymentState employmentState,
        @Schema(description = "The abbreviation of the member.", example = "SS", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1, maxLength = 3) String abbreviation,
        @Schema(description = "The date of hire of the member.", example = "2025-09-24", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Date dateOfHire,
        @Schema(description = "The birth date of the member", example = "1995-02-19", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Date birthDate,
        @Schema(description = "Whether the member is an admin or not.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) boolean isAdmin,
        @Schema(description = "The Organisation unit of the member.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false, minLength = 1) Long organisationUnitId) {
}