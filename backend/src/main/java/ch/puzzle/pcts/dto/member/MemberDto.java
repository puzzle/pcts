package ch.puzzle.pcts.dto.member;

import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.model.member.EmploymentState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberDto(
        @Schema(description = "The unique identifier of the member.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "The first name of member.", example = "Susi", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String firstName,
        @Schema(description = "The last name of the member.", example = "Miller", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) String lastName,
        @Schema(description = "The employment state of the member.", example = "APPLICANT", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1) EmploymentState employmentState,
        @Schema(description = "The abbreviation of the member.", example = "SM", requiredMode = Schema.RequiredMode.NOT_REQUIRED) String abbreviation,
        @Schema(description = "The member's hire date.", example = "2025-09-24", requiredMode = Schema.RequiredMode.NOT_REQUIRED) LocalDate dateOfHire,
        @Schema(description = "The member's birth date.", example = "1995-02-19", requiredMode = Schema.RequiredMode.REQUIRED) LocalDate birthDate,
        @Schema(description = "The Organisation unit of the member.", requiredMode = Schema.RequiredMode.NOT_REQUIRED) OrganisationUnitDto organisationUnit,
        @Schema(description = "The unique identifier of the member from the PuzzleTime", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED) Long ptimeId,
        @Schema(description = "The timestamp of the last time the sync with the PuzzleTime API was successful", example = "2024-02-14T10:15:30", requiredMode = Schema.RequiredMode.NOT_REQUIRED) LocalDateTime lastSuccessfulSync,
        @Schema(description = "The amount of times the sync failed since it was last successful ", example = "3", requiredMode = Schema.RequiredMode.NOT_REQUIRED) Integer syncErrorCount) {
}