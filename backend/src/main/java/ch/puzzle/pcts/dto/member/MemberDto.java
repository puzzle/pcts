package ch.puzzle.pcts.dto.member;

import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.model.member.EmploymentState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record MemberDto(
        @Schema(description = "The unique identifier of the member.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The first name of member.", example = "Susi", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String firstName,
        @Schema(description = "The last name of the member.", example = "Miller", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String lastName,
        @Schema(description = "The employment state of the member.", example = "APPLICANT", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) EmploymentState employmentState,
        @Schema(description = "The abbreviation of the member.", example = "SM", requiredMode = Schema.RequiredMode.NOT_REQUIRED) String abbreviation,
        @Schema(description = "The member's hire date.", example = "2025-09-24", requiredMode = Schema.RequiredMode.REQUIRED) LocalDate dateOfHire,
        @Schema(description = "The member's birth date.", example = "1995-02-19", requiredMode = Schema.RequiredMode.REQUIRED) LocalDate birthDate,
        @Schema(description = "The Organisation unit of the member.", exampleClasses = OrganisationUnitDto.class, requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) OrganisationUnitDto organisationUnit) {
}